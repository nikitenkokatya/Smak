package com.example.smak.buscar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.smak.Locator
import com.example.smak.R
import com.example.smak.data.Receta
import com.example.smak.data.RecetaAPIDetails
import com.example.smak.databinding.FragmentApiDetailBinding
import com.example.smak.databinding.FragmentDetailBinding
import com.example.smak.perfil.ui.usecase.GuardadasViewModel
import com.example.smak.ui.adapter.PhotoListAdapter


class ApiDetailFragment : Fragment() {
    private var _binding: FragmentApiDetailBinding? = null
    private val binding get() = _binding!!
    //private val viewmodel: GuardadasViewModel by viewModels()


    //private lateinit var saveMenuItem: MenuItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setUpToolbar()
        val receta = requireArguments().getParcelable<RecetaAPIDetails>(RecetaAPIDetails.TAG)
        binding.receta = receta

        receta!!.let {
            // Parte Foto
            Glide.with(Locator.requieredApplication)
                .load(receta.image)
                .into(binding.imgfotodetail)

            // Parte Ingredientes
            val ingredients = binding.receta!!.extendedIngredients.map { it.original }
            var ingredientsText = ""

            ingredients.forEach {
               ingredientsText += "· $it\n"
            }

            binding.textView8.text = ingredientsText

            // Parte Pasos
            var stepsText = ""
            binding.receta!!.analyzedInstructions[0].steps.forEach {
                stepsText += "- Step ${it.number} -> ${it.step}\n\n"
            }

            binding.textView9.text= stepsText

            // Parte Duracion
            val totalDuration = binding.receta!!.preparationMinutes + binding.receta!!.cookingMinutes
            binding.textView10.text = formatMinutes(totalDuration)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApiDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun formatMinutes(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60

        return if (hours > 0 && remainingMinutes > 0) {
            "$hours h $remainingMinutes min"
        } else if (hours > 0) {
            "$hours h"
        } else {
            "$remainingMinutes min"
        }
    }
}