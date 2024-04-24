package com.example.smak

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.smak.databinding.FragmentFirstBinding
import com.example.smak.databinding.FragmentWelcomeBinding
import com.google.firebase.auth.FirebaseAuth


class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_FirstFragment)
        }
        binding.btnGoToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_SecondFragment)
        }
    }

    private fun login() {
        (activity as MainActivity).setBottomNavVisible()
        findNavController().navigate(R.id.action_welcomeFragment_to_smakFragment)
    }

    private fun isLoggedIn(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).setBottomNavGone()

        if (isLoggedIn()) {
            login()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}