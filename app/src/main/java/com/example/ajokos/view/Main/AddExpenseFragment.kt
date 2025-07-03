package com.example.ajokos.view.Main

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ajokos.R
import com.example.ajokos.databinding.FragmentAddExpenseBinding
import com.example.ajokos.model.SessionManager
import com.example.ajokos.model.data.Budget
import com.example.ajokos.model.data.Expenses
import com.example.ajokos.viewmodel.BudgetViewModel
import com.example.ajokos.viewmodel.ExpensesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddExpenseFragment : Fragment() {
    private lateinit var binding: FragmentAddExpenseBinding
    private lateinit var expensesViewModel: ExpensesViewModel
    private lateinit var budgetViewModel: BudgetViewModel

    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedBudget: Budget? = null
    private var budgetList: List<Budget> = listOf()
    private var userId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expensesViewModel = ViewModelProvider(this)[ExpensesViewModel::class.java]
        budgetViewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        userId = SessionManager(requireContext()).getUserId()

        setupDateField()
        observeBudgetList()
        setupListeners()
        loadBudgetsForSelectedDate()
    }

    private fun setupDateField() {
        updateDateField()
        binding.etExpenseDate.setOnClickListener {
            val now = selectedDate
            DatePickerDialog(
                requireContext(), { _, year, month, dayOfMonth ->
                    selectedDate.set(year, month, dayOfMonth)
                    updateDateField()
                    loadBudgetsForSelectedDate()
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateField() {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id"))
        binding.etExpenseDate.setText(sdf.format(selectedDate.time))
    }

    private fun loadBudgetsForSelectedDate() {
        val month = selectedDate.get(Calendar.MONTH) + 1
        val year = selectedDate.get(Calendar.YEAR)
        budgetViewModel.getBudgetsByMonth(userId, month)
    }

    private fun observeBudgetList() {
        budgetViewModel.budgetLD.observe(viewLifecycleOwner) { list ->
            budgetList = list
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                list.map { it.name }
            )
            binding.spinnerBudgetOptions.adapter = adapter

            binding.spinnerBudgetOptions.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedBudget = budgetList.getOrNull(position)
                        updateBudgetProgress()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        selectedBudget = null
                        updateBudgetProgress()
                    }
                }

            // Set default
            selectedBudget = budgetList.firstOrNull()
            updateBudgetProgress()
        }
    }

    private fun updateBudgetProgress() {
        selectedBudget?.let { budget ->
            binding.txtActualBudget.text = "Budget: ${budget.budget}"
            binding.txtBudgetLeft.text = "Left: ${budget.budgetLeft}"

            val progress = ((budget.budgetLeft.toFloat() / budget.budget.toFloat()) * 100).toInt()
            binding.progressBudget.max = 100
            binding.progressBudget.progress = progress
        }
    }

    private fun setupListeners() {
        binding.btnAddExpense.setOnClickListener {
            val nominalStr = binding.etExpenseAmount.text.toString()
            val note = binding.etExpenseNote.text.toString()

            if (nominalStr.isEmpty()) {
                Toast.makeText(requireContext(), "Nominal tidak boleh kosong", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val nominal = nominalStr.toInt()
            val budget = selectedBudget
            if (budget == null) {
                Toast.makeText(requireContext(), "Pilih budget terlebih dahulu", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (nominal > budget.budgetLeft) {
                Toast.makeText(requireContext(), "Nominal melebihi sisa budget", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val expense = Expenses(
                date = selectedDate.timeInMillis,
                budgetId = budget.id,
                nominal = nominal,
                description = note,
                userId = userId
            )

            expensesViewModel.addExpenseAndUpdateBudget(expense, budget)

            Toast.makeText(requireContext(), "Pengeluaran berhasil ditambahkan", Toast.LENGTH_SHORT)
                .show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }


}