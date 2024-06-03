package com.example.smak

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smak.databinding.FragmentTipoBinding
import com.example.smak.databinding.ItemTipoBinding
import com.example.smak.ui.adapter.RecetaAdapter
import com.google.firebase.firestore.core.OrderBy.Direction


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
            Tipo("Desayuno", R.mipmap.ic_postree),
            Tipo("Almuerzo", R.mipmap.ic_postre),
            Tipo("Cena", R.drawable.ic_postre),
            Tipo("Postre", R.drawable.ic_postre)
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


class RecipeTypeAdapter(
    private val types: List<Tipo>,
    private val onClick: (String) -> Unit) : RecyclerView.Adapter<RecipeTypeAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemTipoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Tipo) {
            binding.txttipo.text = item.name
            Glide.with(binding.imgftipo.context)
                .load(item.imageRes)
                .into(binding.imgftipo)
            binding.executePendingBindings()
            binding.root.setOnClickListener { onClick(item.name) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTipoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(types[position])
    }

    override fun getItemCount() = types.size
}


data class Tipo(
    val name: String,
    val imageRes: Int
)