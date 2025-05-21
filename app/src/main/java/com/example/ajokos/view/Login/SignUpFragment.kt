package com.example.ajokos.view.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ajokos.R
import com.example.ajokos.databinding.FragmentSignUpBinding
import com.example.ajokos.model.data.User
import com.example.ajokos.model.database.AppDatabase
import com.example.ajokos.viewmodel.UserRepository
import com.example.ajokos.viewmodel.UserViewModel
import com.example.ajokos.viewmodel.UserViewModelFactory


class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = requireActivity().application
        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val repository = UserRepository(userDao) // <-- ini perlu kamu tambahkan
        val factory = UserViewModelFactory(repository)
        val viewModel = ViewModelProvider(
            this,
            UserViewModelFactory(repository)
        )[UserViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val name = binding.txtName.text.toString()
            val password = binding.txtPassword.text.toString()

            if (username.isBlank() || name.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val newUser = User(username = username, name = name, password = password)
                viewModel.register(newUser) { success, message ->
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        if (success) {
                            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                        }
                    }
                }
            }
        }
        binding.tvToSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
