package com.example.smak.comentarios

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smak.R
import com.example.smak.comentarios.adapter.ComentariosAdapter
import com.example.smak.comentarios.usecase.ComentariosViewModel
import com.example.smak.data.Comentario
import com.example.smak.data.Receta
import com.example.smak.databinding.FragmentComentariosBinding


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
                toggleEmptyCommentsImage(comentarios.isEmpty())
            }

            comentariosViewModel.loadComentarios(recetaNombre)
        }
    }
    private fun toggleEmptyCommentsImage(show: Boolean) {
        if (show) {
            binding.imgcomment.visibility = View.VISIBLE
            binding.recyclerView.visibility = GONE
        } else {
            binding.imgcomment.visibility = GONE
            binding.recyclerView.visibility = View.VISIBLE
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