package com.example.smak

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smak.data.Comentario
import com.example.smak.data.Receta
import com.example.smak.database.repository.ComentarioRepository
import com.example.smak.database.repository.PerfilRepository
import com.example.smak.database.repository.RecetaRepository
import com.example.smak.database.resource.Resource
import com.example.smak.databinding.FragmentComentariosBinding
import com.example.smak.databinding.ItemMessageBinding
import com.example.smak.ui.adapter.RecetaAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ComentariosFragment : Fragment(), ComentariosAdapter.onClick {
    private var _binding: FragmentComentariosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ComentariosAdapter
    private lateinit var recetaNombre: String
    private val comentariosViewModel: ComentariosViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentComentariosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("comentarios", Context.MODE_PRIVATE)

        val receta = requireArguments().getParcelable<Receta>(Receta.TAG)
        receta?.let {
            recetaNombre = it.nombre
            Log.d("ComentariosFragment", "Receta: $recetaNombre")

            if (hasUserCommented(recetaNombre)) {
                disableCommenting()
            } else {
                binding.sendButton.setOnClickListener {
                    val messageText = binding.messageEditText.text.toString().trim()
                    val valorEstrellas = binding.ratingBar.rating
                    if (messageText.isNotEmpty()) {
                        comentariosViewModel.sendMessage(recetaNombre, messageText, valorEstrellas)
                        binding.messageEditText.text.clear()
                        setUserCommented(recetaNombre)
                        disableCommenting()
                    }
                    Toast.makeText(context, "Gracias por añadir un comentario",  Toast.LENGTH_SHORT).show()

                }

            }
            initRecyclerView()

            comentariosViewModel.comentarios.observe(viewLifecycleOwner) { comentarios ->
                adapter.submitList(comentarios)
            }

            comentariosViewModel.loadComentarios(recetaNombre)
        }
    }

    private fun initRecyclerView() {
        adapter = ComentariosAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun hasUserCommented(recetaNombre: String): Boolean {
        return sharedPreferences.getBoolean(recetaNombre, false)
    }

    private fun setUserCommented(recetaNombre: String) {
        sharedPreferences.edit().putBoolean(recetaNombre, true).apply()
    }

    private fun disableCommenting() {
        binding.messageEditText.visibility = GONE
        binding.sendButton.visibility = GONE
        binding.ratingBar.visibility = GONE
    }
    override fun onClickPersona(comentario: Comentario) {
        val bundle = Bundle().apply {
            putString("email", comentario.email)
        }
        findNavController().navigate(R.id.action_comentariosFragment_to_personaFragment, bundle)
    }
}

class ComentariosAdapter (private val listener: onClick): ListAdapter<Comentario, ComentarioViewHolder>(COMENTARIO_COMPARATOR) {
    interface onClick {
        fun onClickPersona(comentario: Comentario)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentarioViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return  ComentarioViewHolder(ItemMessageBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ComentarioViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)

        holder.binding.root.setOnClickListener() { _ ->
            listener.onClickPersona(item)
        }
    }

    companion object {
        val COMENTARIO_COMPARATOR = object : DiffUtil.ItemCallback<Comentario>() {
            override fun areItemsTheSame(oldItem: Comentario, newItem: Comentario): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: Comentario, newItem: Comentario): Boolean {
                return newItem.autor == oldItem.autor
            }
        }
    }
}

class ComentarioViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comentario: Comentario) {
        binding.senderTextView.text = comentario.autor
        binding.messageTextView.text = comentario.contenido
        binding.txtfecha.text = comentario.fecha
        binding.estrellas.rating = comentario.estrellas
    }
}

class ComentariosViewModel : ViewModel() {

    private val comentarioRepository = ComentarioRepository()

    private val _comentarios = MutableLiveData<List<Comentario>>()
    val comentarios: LiveData<List<Comentario>> get() = _comentarios

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loadComentarios(recetaNombre: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = ComentarioRepository.getComentarios(recetaNombre)
            if (result is Resource.Success<*>) {
                _comentarios.value = result.data as List<Comentario>
            } else {
            }
            _loading.value = false
        }
    }

    fun sendMessage(recetaNombre: String, contenido: String, estrellas: Float) {
        viewModelScope.launch {
            val currentMillis = System.currentTimeMillis()
            val currentDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentMillis), ZoneId.systemDefault())
            val formattedDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val autor = obtenerNombreAutor()
            if (autor != null) {
                val comentario = Comentario(autor, "", contenido, formattedDateTime, estrellas) // Incluir estrellas en el objeto Comentario

                val result = ComentarioRepository.agregarComentario(recetaNombre, comentario)
                if (result is Resource.Success<*>) {
                    loadComentarios(recetaNombre)
                }
            }
        }
    }

    suspend fun obtenerNombreAutor(): String {
        val currentUser = auth.currentUser
        val email = currentUser?.email ?: return "Unknown"

        val perfilRepository = PerfilRepository()
        return try {
            val perfil = perfilRepository.getPerfilSuspend(email)
            perfil?.nombre ?: email.substringBefore("@")
        } catch (e: Exception) {
            email.substringBefore("@")
        }
    }

}