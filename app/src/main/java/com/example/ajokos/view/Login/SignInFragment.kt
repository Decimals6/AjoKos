package com.example.ajokos.view.Login

import android.content.Intent
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
import com.example.ajokos.databinding.FragmentSignInBinding
import com.example.ajokos.model.SessionManager
import com.example.ajokos.model.database.AppDatabase
import com.example.ajokos.view.Main.MainActivity
import com.example.ajokos.viewmodel.UserRepository
import com.example.ajokos.viewmodel.UserViewModel

class SignInFragment : Fragment() {

    private lateinit var binding :FragmentSignInBinding

    private lateinit var userViewModel: UserViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPassword.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Harap isi semua field", Toast.LENGTH_SHORT).show()
            } else {
                userViewModel.login(username, password)
            }
        }
        binding.tvToSignUp.setOnClickListener {
            val action = SignInFragmentDirections.actionToSignUpFragment()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.loginResult.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

                if (it == "Login berhasil") {
                    // Simpan user ID ke session
                    userViewModel.loggedInUserId.value?.let { userId ->
                        sessionManager.saveLogin(userId)
                    }

                    // Pindah ke MainActivity
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

                // Reset hasil login biar tidak ke-trigger ulang
                userViewModel.clearLoginResult()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
