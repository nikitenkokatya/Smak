package com.example.smak

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smak.data.Receta
import com.example.smak.database.repository.PerfilRepository
import com.example.smak.database.repository.RecetaRepository
import com.example.smak.databinding.FragmentPersonaBinding
import com.example.smak.databinding.PerfilLayoutBinding
import com.example.smak.perfil.ui.adapter.CreadasAdapter
import com.example.smak.perfil.ui.usecase.PerfilViewModel
import com.example.smak.ui.adapter.RecetaAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.min


class PersonaFragment : Fragment(), PerosonaAdapter.onClickCreadas {
    private var _binding: FragmentPersonaBinding? = null
    private val binding get() = _binding!!
    private lateinit var email: String


    private val perfilViewModel: PerfilViewModel by viewModels()
    private val recetaViewModel: PersonaViewModel by viewModels()
    private lateinit var adapterCreadas: PerosonaAdapter
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = requireArguments().getString("email") ?: return

        loadPerfil(email)
        recetaViewModel.obtenerRecetasPrivadas(email, binding.txtcantidad)
        recetaViewModel.recetasPrivadas.observe(viewLifecycleOwner) { recetas ->
            adapterCreadas.submitList(recetas)
        }

        initrvCreadas()
    }

    private fun loadPerfil(email: String) {
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        perfilViewModel.getPerfil(email, {
            // onSuccess
            binding.imgpersona.setImageBitmap(
                applyCircularMask(
                    base64ToBitmap(PerfilRepository.userPerfil!!.foto!!)
                )
            )
            binding.txtnperson.text = PerfilRepository.userPerfil!!.nombre
        }, {
            binding.imgpersona.setImageResource(R.drawable.user)
            val userEmail = user!!.email ?: ""
            val userNameBeforeAt = userEmail.substringBefore('@')
            binding.txtnperson.text = userNameBeforeAt
        })
    }

    fun initrvCreadas() {
        adapterCreadas = PerosonaAdapter(this)
        binding.rvprecetas.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvprecetas.adapter = adapterCreadas
    }

    fun base64ToBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    private fun applyCircularMask(bitmap: Bitmap): Bitmap {
        val size = min(bitmap.width, bitmap.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint().apply {
            isAntiAlias = true
            shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
        val radius = size / 2f
        canvas.drawCircle(radius, radius, radius, paint)
        return output
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickDetailsC(receta: Receta) {
        var bundle = Bundle()
        bundle.putParcelable(Receta.TAG, receta)

        findNavController().navigate(R.id.action_personaFragment_to_detailFragment, bundle)
    }
}

class PerosonaAdapter(private val listener: onClickCreadas) :
    ListAdapter<Receta, PRecetaViewHolder>(LIST_COMPARATOR) {

    interface onClickCreadas {
        fun onClickDetailsC(receta: Receta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PRecetaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PRecetaViewHolder(PerfilLayoutBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: PRecetaViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, Locator.requieredApplication)

        holder.binding.root.setOnClickListener() {
            listener.onClickDetailsC(item)
        }
    }

    companion object {
        val LIST_COMPARATOR = object : DiffUtil.ItemCallback<Receta>() {
            override fun areItemsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: Receta, newItem: Receta): Boolean {
                return newItem.nombre == oldItem.nombre
            }
        }
    }
}

class PRecetaViewHolder(val binding: PerfilLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Receta, context: Context) {
        val img = ImageView(context)
        img.setImageBitmap(base64ToBitmap(item.imagenes[0]))

        binding.llParent.addView(img);


        val screenWidth: Int = context.getResources().getDisplayMetrics().widthPixels
        val imageViewWidth = screenWidth / 3

        val layoutParams = ConstraintLayout.LayoutParams(
            imageViewWidth,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        img.setLayoutParams(layoutParams)

        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

        img.setLayoutParams(layoutParams)
    }

    fun base64ToBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}


class PersonaViewModel : ViewModel() {
    private val recetaRepository = RecetaRepository()

    private val _recetasPrivadas = MutableLiveData<List<Receta>>()
    val recetasPrivadas: LiveData<List<Receta>> get() = _recetasPrivadas

    fun obtenerRecetasPrivadas(email: String, textView: TextView) {
        recetaRepository.getRecetasPrivadas(email) { recetas ->
            _recetasPrivadas.value = recetas
            textView.text = recetas.size.toString()

        }
    }
}
