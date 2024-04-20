package com.example.smak

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.smak.databinding.FragmentFirstBinding
import com.example.smak.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        (activity as MainActivity).setBottomNavGone()

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}