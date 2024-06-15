package com.example.smak.comentarios

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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smak.R
import com.example.smak.comentarios.adapter.PerosonaAdapter
import com.example.smak.comentarios.usecase.PersonaViewModel
import com.example.smak.data.Receta
import com.example.smak.database.repository.PerfilRepository
import com.example.smak.databinding.FragmentPersonaBinding
import com.example.smak.perfil.ui.usecase.PerfilViewModel
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
        perfilViewModel.getPerfil(email, {
            // onSuccess
            binding.imgpersona.setImageBitmap(
                applyCircularMask(
                    base64ToBitmap(PerfilRepository.userPerfil!!.foto!!)
                )
            )
            (activity as AppCompatActivity).supportActionBar?.title = PerfilRepository.userPerfil!!.nombre
        }, {
            binding.imgpersona.setImageResource(R.drawable.user)
            val userEmail = email ?: ""
            val userNameBeforeAt = userEmail.substringBefore('@')
            (activity as AppCompatActivity).supportActionBar?.title = userNameBeforeAt
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