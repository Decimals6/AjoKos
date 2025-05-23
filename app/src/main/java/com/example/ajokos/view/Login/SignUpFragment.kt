package com.example.ajokos.view.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.ajokos.R
import com.example.ajokos.databinding.FragmentSignUpBinding
import com.example.ajokos.model.data.User
import com.example.ajokos.model.database.AppDatabase
import com.example.ajokos.viewmodel.UserViewModel


class SignUpFragment : Fragment() {

    private lateinit var binding : FragmentSignUpBinding

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            val fname = binding.txtFname.text.toString()
            val lname = binding.txtLname.text.toString()
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPassword.text.toString()

            if (fname.isBlank() || lname.isBlank() || username.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(
                fname = fname,
                lname = lname,
                username = username,
                password = password,
                name = "$fname $lname"
            )

            userViewModel.registerUser(user)
        }
        binding.tvToSignIn.setOnClickListener {
            val action = SignUpFragmentDirections.actionToSignInFragment()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.signupResult.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            if (message == "Pendaftaran berhasil") {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
