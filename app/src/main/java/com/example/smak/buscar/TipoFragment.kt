package com.example.smak.buscar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smak.MainActivity
import com.example.smak.R
import com.example.smak.buscar.adapter.RecipeTypeAdapter
import com.example.smak.data.Tipo
import com.example.smak.databinding.FragmentBuscadorBinding
import com.example.smak.databinding.FragmentTipoBinding
import com.example.smak.databinding.ItemTipoBinding


class TipoFragment : Fragment() {

    private var _binding: FragmentTipoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTipoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tipos = listOf(
            Tipo("Desayuno", R.drawable.desayuno),
            Tipo("Almuerzo", R.drawable.almuerzo),
            Tipo("Postre", R.drawable.postre),
            Tipo("Cena", R.drawable.cena)
        )

        val adapter = RecipeTypeAdapter(tipos) { type ->
            val bundle = Bundle().apply {
                putString("tipo", type)
            }
            findNavController().navigate(R.id.action_tipoFragment_to_buscadorFragment2, bundle)
        }
        binding.viewPager.layoutManager = LinearLayoutManager(requireContext())
        binding.viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).setBottomNavVisible()
        (activity as MainActivity).setDefaultHighlight()
    }
}