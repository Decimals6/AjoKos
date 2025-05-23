package com.example.ajokos.view.Main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.ajokos.R
import com.example.ajokos.databinding.FragmentAddEditBudgetBinding
import com.example.ajokos.model.data.Budget
import com.example.ajokos.model.data.User
import com.example.ajokos.viewmodel.BudgetViewModel
import com.example.ajokos.viewmodel.UserViewModel

class AddBudgetFragment : Fragment() {
    private lateinit var binding: FragmentAddEditBudgetBinding
    private lateinit var viewModel: BudgetViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditBudgetBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        binding.btnAddBudget.setOnClickListener {
            userViewModel.loggedInUserId.observe(viewLifecycleOwner) { idUser ->
                if (idUser != null) {
                    val name = binding.txtBudgetName.text.toString()
                    val budget = binding.txtBudgetAmount.text.toString().toIntOrNull()
                    val budgetLeft = binding.txtBudgetAmount.text.toString().toIntOrNull()
                    val userid = idUser

                    if (name.isBlank() || budget == null || budgetLeft == null){
                        Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show()

                    } else {
                        val budget = Budget(
                            name = name,
                            budget = budget,
                            budgetLeft = budgetLeft,
                            userId = userid
                        )

                        viewModel.addBudget(budget)
                        Toast.makeText(view.context, "Data added", Toast.LENGTH_LONG).show()
                        Navigation.findNavController(it).popBackStack()
                    }

                } else {
                    Toast.makeText(requireContext(), "User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddBudgetFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}