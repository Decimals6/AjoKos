package com.example.ajokos.view.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ajokos.R
import com.example.ajokos.databinding.FragmentSignInBinding
import com.example.ajokos.model.database.AppDatabase
import com.example.ajokos.viewmodel.UserRepository
import com.example.ajokos.viewmodel.UserViewModel
import com.example.ajokos.viewmodel.UserViewModelFactory

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val app = requireActivity().application
//        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
//        val repository = UserRepository(userDao)
//        val viewModel = ViewModelProvider(
//            this,
//            UserViewModelFactory(repository)
//        )[UserViewModel::class.java]
//        binding.btnLogin.setOnClickListener {
//            // Nanti diisi logic login
//        }

        // Klik "Belum punya akun? Daftar"
        binding.tvToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
