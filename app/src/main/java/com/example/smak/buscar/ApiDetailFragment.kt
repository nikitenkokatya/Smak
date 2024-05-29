package com.example.smak.buscar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.smak.Locator
import com.example.smak.R
import com.example.smak.data.Receta
import com.example.smak.data.RecetaAPIDetails
import com.example.smak.databinding.FragmentApiDetailBinding


class ApiDetailFragment : Fragment(), MenuProvider {
    private var _binding: FragmentApiDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var saveMenuItem: MenuItem
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

        setUpToolbar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApiDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun setUpToolbar() {
        val menuhost: MenuHost = requireActivity()
        menuhost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.traducir_menu, menu)
        saveMenuItem = menu.findItem(R.id.action_traducir)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_traducir -> {
                val ingredients = binding.receta!!.extendedIngredients.map { it.original }
                var ingredientsText = ""

                ingredients.forEach {
                    ingredientsText += "· $it\n"
                }

                var stepsText = ""
                binding.receta!!.analyzedInstructions[0].steps.forEach {
                    stepsText += "- Step ${it.number} -> ${it.step}\n\n"
                }

                // Realizar la traducción de cada parte de la receta


                true
            }
            R.id.action_compartir ->{
                val ingredients = binding.receta!!.extendedIngredients.map { it.original }
                var ingredientsText = ""

                ingredients.forEach {
                    ingredientsText += "· $it\n"
                }

                var stepsText = ""
                binding.receta!!.analyzedInstructions[0].steps.forEach {
                    stepsText += "- Step ${it.number} -> ${it.step}\n\n"
                }
                val textoCompartir = "Te comparto esta receta: ${binding.receta?.title}\n" +
                        "Ingredientes: ${ingredientsText}\n" +
                        "Pasos: ${stepsText}"


                val compartirIntent = Intent(Intent.ACTION_SEND)
                compartirIntent.type = "text/plain"
                compartirIntent.putExtra(Intent.EXTRA_TEXT, textoCompartir)


                if (compartirIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(compartirIntent)
                } else {
                    Toast.makeText(requireContext(), "No se encontró ninguna aplicación para compartir", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> {
                false
            }
        }
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