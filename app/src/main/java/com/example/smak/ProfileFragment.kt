package com.example.smak

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smak.databinding.FragmentProfileBinding
import com.example.smak.ui.adapter.RecetaAdapter
import com.example.smak.ui.usecase.ListState
import com.example.smak.ui.usecase.ListViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterCreadas: RecetaAdapter
    private lateinit var adapterGuardadas: RecetaAdapter
    private val viewmodel: ListViewModel by viewModels()


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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val user = mAuth.currentUser

        if (user != null) {
            val userEmail = user?.email ?: ""
            val userNameBeforeAt = userEmail.substringBefore('@')
            binding.textView.text = userNameBeforeAt
        }
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding.btnSalir.setOnClickListener {
            signOutAndStartSignInActivity()
        }


        adapterCreadas = RecetaAdapter()
        adapterGuardadas = RecetaAdapter()
        binding.rvcreadas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvguardadas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvcreadas.adapter = adapterCreadas
        binding.rvguardadas.adapter = adapterGuardadas

        viewmodel.recetas.observe(viewLifecycleOwner, Observer { recetas ->
            adapterCreadas.submitList(recetas)
        })

        viewmodel.getState().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ListState.Success -> onSuccess()
                is ListState.Error -> onNoError()
            }
        })

        viewmodel.getAllRecetas()
        binding.btnnavigate.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_creadas -> {
                    binding.rvcreadas.visibility = View.VISIBLE
                    binding.rvguardadas.visibility = View.GONE

                    true
                }
                R.id.nav_guardar -> {
                    binding.rvcreadas.visibility = View.GONE
                    binding.rvguardadas.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }
        binding.rvcreadas.visibility = View.VISIBLE
        binding.rvguardadas.visibility = View.GONE
    }

    private fun signOutAndStartSignInActivity() {
        mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            findNavController().navigate(R.id.action_profileFragment2_to_welcomeFragment)
        }
    }

    fun onSuccess(){
        //binding.imageView.visibility = GONE
        binding.rvcreadas.visibility = View.VISIBLE
    }
    fun onNoError(){
        //binding.imageView.visibility = VISIBLE
        binding.rvguardadas.visibility = View.GONE
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}