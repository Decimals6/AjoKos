package com.example.ajokos.view.Main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ajokos.R
import com.example.ajokos.databinding.FragmentHistoryExpensesBinding
import com.example.ajokos.model.SessionManager
import com.example.ajokos.viewmodel.ExpensesViewModel
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Locale


class HistoryExpensesFragment : Fragment() {
    private lateinit var binding: FragmentHistoryExpensesBinding
    private lateinit var viewModel: ExpensesViewModel
    private lateinit var expensesAdapter: ExpensesAdapter
    private var monthMap: Map<String, String> = mapOf() // Mapping nama bulan ke angka

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ExpensesViewModel::class.java]
        val sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        expensesAdapter = ExpensesAdapter(requireContext(), arrayListOf())
        binding.recviewExpenses.layoutManager = LinearLayoutManager(requireContext())
        binding.recviewExpenses.adapter = expensesAdapter

        // Back button
        binding.btnBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        // Fetch available months
        viewModel.fetchAvailableMonths(userId)
        observeViewModel(userId)
    }

    private fun observeViewModel(userId: Int) {
        viewModel.availableMonthsLD.observe(viewLifecycleOwner) { months ->
            val monthNames = months.map { getMonthName(it) }
            monthMap = months.zip(monthNames).associate { it.second to it.first }

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, monthNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerMonth.adapter = adapter

            // Trigger load data default (bulan pertama di list)
            if (months.isNotEmpty()) {
                val firstMonth = months.first()
                viewModel.getExpensesByMonth(userId, firstMonth)
            }

            binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val selectedMonthName = parent?.getItemAtPosition(position).toString()
                    val selectedMonthNum = monthMap[selectedMonthName]
                    selectedMonthNum?.let {
                        viewModel.getExpensesByMonth(userId, it)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Optional: bisa dikosongin
                }
            }
            Log.d("BULAN TERSEDIA", "Raw bulan: $months")
            Log.d("MAPPING", "Map: $monthMap")

        }

        viewModel.expensesLD.observe(viewLifecycleOwner) { list ->
            expensesAdapter.updateExpensesList(list)

            if (list.isEmpty()) {
                binding.recviewExpenses.visibility = View.GONE
                binding.txtError.visibility = View.VISIBLE
            } else {
                binding.recviewExpenses.visibility = View.VISIBLE
                binding.txtError.visibility = View.GONE
            }
        }

    }
    fun getMonthName(monthStr: String): String {
        val parts = monthStr.split("-")
        val month = parts[0].toInt()
        val year = parts[1]

        val monthName = DateFormatSymbols(Locale("id")).months[month - 1]
        return "${monthName.replaceFirstChar { it.uppercase() }} $year"
    }


}