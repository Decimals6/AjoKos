package com.example.ajokos.view.Main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.ajokos.R
import com.example.ajokos.databinding.FragmentAddEditBudgetBinding
import com.example.ajokos.model.data.Budget
import com.example.ajokos.viewmodel.BudgetViewModel

class EditBudgetFragment : Fragment() {
    private lateinit var binding: FragmentAddEditBudgetBinding
    private lateinit var viewModel: BudgetViewModel
    private lateinit var budget: Budget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)
        binding.txtTitle.text = "Edit Budget"
        binding.btnAddBudget.text = "Save Change"

        val currentDate = java.util.Calendar.getInstance()
        val currentMonth = currentDate.get(java.util.Calendar.MONTH) + 1 // +1 karena Januari = 0
        val currentYear = currentDate.get(java.util.Calendar.YEAR)

        // retrieve the argument from clicked+ imgEdit
        val id = EditBudgetFragmentArgs.fromBundle(requireArguments()).id
        viewModel.getBudgetById(id)
        observeViewModel()
        binding.btnAddBudget.setOnClickListener {
            val vname = binding.txtBudgetName.text.toString()
            val vbudget = binding.txtBudgetAmount.text.toString().toInt()

            if (budget.budgetSpend < vbudget){
                val budgetEntity = Budget(
                    name = vname,
                    budget = vbudget,
                    budgetSpend = budget.budgetSpend,
                    budgetLeft = vbudget - budget.budgetSpend,
                    userId = budget.userId,
                    month = currentMonth,
                    year = currentYear
                )
                viewModel.updateBudget(budgetEntity.name, budgetEntity.budget, budgetEntity.budgetLeft, budgetEntity.budgetSpend, budget.id )
                Toast.makeText(requireContext(), "Data Berhasil diubah", Toast.LENGTH_LONG).show()
                Navigation.findNavController(it).popBackStack() // lebih clean
            } else {
                Toast.makeText(requireContext(), "Nilai Budget Diubah tidak boleh melebihi expenses", Toast.LENGTH_LONG).show()
            }
//            Toast.makeText(requireContext(), budget.id.toString() +
//                " budget: " + budget.budget.toString() + " budgetleft: " + budget.budgetLeft.toString()
//                + " name: " + budget.name + " budgetspend: " + budget.budgetSpend.toString(), Toast.LENGTH_LONG).show()
        }
        binding.btnBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

    }
    fun observeViewModel() {
        viewModel.selectedBudget.observe(viewLifecycleOwner, Observer {
            budget = it
            binding.txtBudgetName.setText(it.name)
            binding.txtBudgetAmount.setText(it.budget.toString())
        })
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            EditBudgetFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}