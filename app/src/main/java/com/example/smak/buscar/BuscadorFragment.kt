package com.example.smak.buscar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.smak.api.SpoonacularApiClient
import com.example.smak.databinding.FragmentBuscadorBinding


class BuscadorFragment : Fragment() {
    private var _binding: FragmentBuscadorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuscadorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val apiKey = "cbac8ca59ade4d04b3bd2ab780365021"
        val client = SpoonacularApiClient()

        binding.btnBuscar.setOnClickListener {
            /*val query = binding.tiebuscar.text.toString()
            if (query.isNotEmpty()) {
                client.searchRecipes(query, 10, apiKey) { response ->
                    //val recipes = response.body()?.results ?: emptyList()
                    println(response)
                }
            } else {
                Toast.makeText(requireContext(), "Ingrese un término de búsqueda", Toast.LENGTH_SHORT).show()
            }*/

            client.searchRecipes("pasta", 10, apiKey) { response ->
                //val recipes = response.body()?.results ?: emptyList()
                println(response)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}