package com.example.ajokos.view.Main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ajokos.R
import com.example.ajokos.databinding.FragmentBudgetBinding
import com.example.ajokos.databinding.FragmentSignInBinding
import com.example.ajokos.model.SessionManager
import com.example.ajokos.view.Main.BudgetFragmentDirections
import com.example.ajokos.viewmodel.BudgetViewModel
import com.example.ajokos.viewmodel.UserViewModel

class BudgetFragment : Fragment() {
    private lateinit var binding : FragmentBudgetBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var viewModel: BudgetViewModel
    private lateinit var sessionManager: SessionManager
    private val budgetListAdapter  = BudgetAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        userViewModel.loggedInUserId.observe(viewLifecycleOwner) { idUser ->
            if (idUser != null) {
                viewModel.getBudget(idUser)
            } else {
                Toast.makeText(requireContext(), "User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }

        binding.recviewBudget.layoutManager = LinearLayoutManager(context)
        binding.recviewBudget.adapter = budgetListAdapter

        binding.fabAddBudget.setOnClickListener {
            val action = BudgetFragmentDirections.actionToAddBudgetFragment()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()

    }

    fun observeViewModel() {
        viewModel.budgetLD.observe(viewLifecycleOwner, Observer {
            budgetListAdapter.updateBudgetList(it)
            if(it.isEmpty()) {
                binding.recviewBudget?.visibility = View.GONE
                binding.txtError.visibility = View.VISIBLE
                binding.txtError.setText("Your Budget still empty.")
            } else {
                binding.recviewBudget?.visibility = View.VISIBLE
                binding.txtError.visibility = View.GONE
            }
        })
//        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
//            if(it == false) {
//                binding.progressLoad?.visibility = View.GONE
//            } else {
//                binding.progressLoad?.visibility = View.VISIBLE
//            }
//        })
//        viewModel.todoLoadErrorLD.observe(viewLifecycleOwner, Observer {
//            if(it == false) {
//                binding.txtError?.visibility = View.GONE
//            } else {
//                binding.txtError?.visibility = View.VISIBLE
//            }
//        })
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            BudgetFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}