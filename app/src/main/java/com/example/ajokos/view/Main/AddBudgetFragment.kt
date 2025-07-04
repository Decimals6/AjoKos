package com.example.ajokos.view.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.ajokos.databinding.FragmentAddEditBudgetBinding
import com.example.ajokos.model.SessionManager
import com.example.ajokos.model.data.Budget
import com.example.ajokos.viewmodel.BudgetViewModel
import com.example.ajokos.viewmodel.UserViewModel

class AddBudgetFragment : Fragment() {
    private lateinit var binding: FragmentAddEditBudgetBinding
    private lateinit var viewModel: BudgetViewModel
    private lateinit var userViewModel: UserViewModel
    private var loggedInUserId: Int? = null

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
        val currentDate = java.util.Calendar.getInstance()
        val currentMonth = currentDate.get(java.util.Calendar.MONTH) + 1 // +1 karena Januari = 0
        val currentYear = currentDate.get(java.util.Calendar.YEAR)

        userViewModel.loggedInUserId.observe(viewLifecycleOwner) { idUser ->
            loggedInUserId = idUser
        }

        binding.btnAddBudget.setOnClickListener {
            val name = binding.txtBudgetName.text.toString()
            val budget = binding.txtBudgetAmount.text.toString().toIntOrNull()
            val budgetLeft = budget
            val sessionManager = SessionManager(requireContext())
            val userId = sessionManager.getUserId()

//            println(userId)
//            Toast.makeText(requireContext(), "User ID = " + userId
//                , Toast.LENGTH_SHORT).show()

            if (name.isBlank() || budget == null || userId == null) {
                Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                val budgetEntity = Budget(
                    name = name,
                    budget = budget,
                    budgetLeft = budgetLeft!!,
                    budgetSpend = 0,
                    userId = userId,
                    month = currentMonth,
                    year = currentYear
                )
                viewModel.addBudget(budgetEntity)
                Toast.makeText(requireContext(), "Data added", Toast.LENGTH_LONG).show()
                Navigation.findNavController(it).popBackStack() // lebih clean
            }
        }
        binding.btnBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }
}