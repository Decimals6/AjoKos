package com.example.ajokos.view.Main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ajokos.R
import com.example.ajokos.databinding.FragmentBudgetBinding
import com.example.ajokos.databinding.FragmentProfileBinding
import com.example.ajokos.model.SessionManager
import com.example.ajokos.model.data.User
import com.example.ajokos.view.Login.LoginActivity
import com.example.ajokos.viewmodel.BudgetViewModel
import com.example.ajokos.viewmodel.UserViewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var sessionManager: SessionManager
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        sessionManager = SessionManager(requireContext())
        val sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()
        userViewModel.getUserData(userId)
        observeViewModel()

        binding.btnLogout.setOnClickListener {
            sessionManager.logout()

            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    fun observeViewModel(){
        userViewModel.selectedUser.observe(viewLifecycleOwner, Observer {
            user = it
            binding.tvFirstName.setText(it.fname)
            binding.tvLastName.setText(it.lname)
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}