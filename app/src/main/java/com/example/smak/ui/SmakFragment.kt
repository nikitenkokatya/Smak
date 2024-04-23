package com.example.smak.ui

import android.os.Bundle
import android.service.voice.VoiceInteractionSession.VisibleActivityCallback
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smak.MainActivity
import com.example.smak.R
import com.example.smak.data.Receta
import com.example.smak.databinding.FragmentSmakBinding
import com.example.smak.ui.adapter.RecetaAdapter
import com.example.smak.ui.usecase.ListState
import com.example.smak.ui.usecase.ListViewModel

class SmakFragment : Fragment(), RecetaAdapter.onClick{
    private var _binding: FragmentSmakBinding? = null
    private val binding get() = _binding!!

    lateinit var recetaAdapter: RecetaAdapter
    private val viewmodel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSmakBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRV()

        viewmodel.recetas.observe(viewLifecycleOwner, Observer { recetas ->
            recetaAdapter.submitList(recetas)
        })

        viewmodel.getState().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ListState.Success -> onSuccess()
                is ListState.Error -> onNoError()
            }
        })

        viewmodel.getAllRecetas()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottomNavVisible()
    }

    fun onSuccess(){
        //binding.imageView.visibility = GONE
        binding.rvlista.visibility = VISIBLE
    }
    fun onNoError(){
        //binding.imageView.visibility = VISIBLE
        binding.rvlista.visibility = GONE
    }
    fun initRV(){
        recetaAdapter = RecetaAdapter(this)
        binding.rvlista.layoutManager = LinearLayoutManager(requireContext())
        binding.rvlista.adapter = recetaAdapter
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClickDetails(receta: Receta) {
        var bundle = Bundle()
        bundle.putParcelable(Receta.TAG, receta)

        findNavController().navigate(R.id.action_smakFragment_to_detailFragment, bundle)
    }

    override fun userOnLongClick(receta: Receta): Boolean {
        return true
    }

}