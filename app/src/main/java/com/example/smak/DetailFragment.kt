package com.example.smak

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.smak.data.Receta
import com.example.smak.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar

class DetailFragment : Fragment(), MenuProvider {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: GuardadasViewModel by viewModels()

    private lateinit var saveMenuItem: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        val receta = requireArguments().getParcelable<Receta>(Receta.TAG)
        binding.receta = receta

        viewmodel.recetasFavoritas.observe(viewLifecycleOwner, Observer { recetas ->
            if (viewmodel.recetasFavoritas.value!!.contains(binding.receta))
                saveMenuItem.icon = resources.getDrawable(R.drawable.ic_savemarcado)
            else
                saveMenuItem.icon = resources.getDrawable(R.drawable.ic_guardar)
        })

        viewmodel.cargarRecetasFavoritas()

    }

    private fun setUpToolbar() {
        val menuhost: MenuHost = requireActivity()
        menuhost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.guardar_menu, menu)

        saveMenuItem = menu.findItem(R.id.action_guardar)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_guardar -> {
                if (!viewmodel.recetasFavoritas.value!!.contains(binding.receta)) {
                    saveMenuItem.icon = resources.getDrawable(R.drawable.ic_savemarcado)
                    guardarReceta(binding.receta!!)
                    viewmodel.cargarRecetasFavoritas()
                } else {
                    saveMenuItem.icon = resources.getDrawable(R.drawable.ic_guardar)
                    viewmodel.borrarRecetaFavorita(binding.receta!!)
                    viewmodel.cargarRecetasFavoritas()
                }
                true
            }
            else -> {
                false
            }
        }
    }

    private fun guardarReceta(receta: Receta) {
        viewmodel.agregarRecetaFavorita(receta)
        Snackbar.make(binding.root, "Receta guardada", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}