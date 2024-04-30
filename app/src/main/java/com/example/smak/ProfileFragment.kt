package com.example.smak

import android.app.AlertDialog
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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smak.data.Receta
import com.example.smak.databinding.FragmentProfileBinding
import com.example.smak.ui.usecase.ListState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment(), MenuProvider, CreadasAdapter.onClickCreadas, GuardadasAdapter.onClickGuardadas {
    private lateinit var mAuth: FirebaseAuth

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterCreadas: CreadasAdapter
    private lateinit var adapterGuardadas: GuardadasAdapter
    private val viewmodel: CreadasListViewModel by viewModels()
    private val viewmodelfav: GuardadasViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        val user = mAuth.currentUser

        if (user != null) {
            val userEmail = user?.email ?: ""
            val userNameBeforeAt = userEmail.substringBefore('@')
            binding.textView.text = userNameBeforeAt
        }

        viewmodel.getRecetas().observe(viewLifecycleOwner, Observer { recetas ->
            adapterCreadas.submitList(recetas)
        })

        viewmodelfav.recetasFavoritas.observe(viewLifecycleOwner, Observer { recetas ->
            adapterGuardadas.submitList(recetas.toList())
        })



        viewmodel.getState().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ListState.Success -> onSuccess()
                is ListState.Error -> onNoError()
            }
        })
        setUpToolbar()

        binding.btnnavigate.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_creadas -> {
                    viewmodel.getMisRecetas()
                    binding.rvcreadas.visibility = View.VISIBLE
                    binding.rvguardadas.visibility = View.GONE
                    true
                }
                R.id.nav_guardar -> {
                    viewmodelfav.cargarRecetasFavoritas()
                    binding.rvcreadas.visibility = View.GONE
                    binding.rvguardadas.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }

        initrvCreadas()

        initrvGuardadas()

        binding.rvcreadas.visibility = View.VISIBLE
        binding.rvguardadas.visibility = View.GONE
        viewmodel.getMisRecetas()


        binding.button2.setOnClickListener {
            showEditProfileDialog()
        }

        //binding.txtcreadas.text = countCreadas.toString()
        //binding.txtguardadas.text = countGuardadas.toString()
    }

    fun initrvCreadas(){
        adapterCreadas = CreadasAdapter(this)
        binding.rvcreadas.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvcreadas.adapter = adapterCreadas
    }

    fun initrvGuardadas(){
        adapterGuardadas = GuardadasAdapter(this)
        binding.rvguardadas.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvguardadas.adapter = adapterGuardadas
    }
    private fun showEditProfileDialog() {
        val dialogView = layoutInflater.inflate(R.layout.editarperfil_layout, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()
    }

    private fun setUpToolbar() {
        val menuhost: MenuHost = requireActivity()
        menuhost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(R.id.settingsFragment)
                true
            }
            else -> false
        }
    }
    fun onSuccess(){
        //binding.imageView.visibility = GONE
        //binding.rvcreadas.visibility = View.VISIBLE
    }
    fun onNoError(){
        //binding.imageView.visibility = VISIBLE
        binding.rvguardadas.visibility = View.GONE
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClickDetails(receta: Receta) {
        var bundle = Bundle()
        bundle.putParcelable(Receta.TAG, receta)

        findNavController().navigate(R.id.action_profileFragment2_to_detailFragment, bundle)
    }

    override fun onClickDetailsC(receta: Receta) {
        var bundle = Bundle()
        bundle.putParcelable(Receta.TAG, receta)

        findNavController().navigate(R.id.action_profileFragment2_to_detailFragment, bundle)
    }

    override fun userOnLongClick(receta: Receta): Boolean {
        return true
    }
}