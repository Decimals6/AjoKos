package com.example.ajokos.view.Main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ajokos.R
import com.example.ajokos.databinding.FragmentReportBinding
import com.example.ajokos.model.SessionManager
import com.example.ajokos.viewmodel.BudgetViewModel
import com.example.ajokos.viewmodel.UserViewModel

class ReportFragment : Fragment() {
    private lateinit var binding: FragmentReportBinding
    private lateinit var viewModel: BudgetViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var reportAdapter: ReportAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var monthMap: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        reportAdapter = ReportAdapter(arrayListOf())
        binding.rvReports.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReports.adapter = reportAdapter

        viewModel.fetchAvailableMonths(userId)
        observeViewModel(userId)
    }

    private fun observeViewModel(userId: Int) {
        viewModel.availableMonthsLD.observe(viewLifecycleOwner) { monthList ->
            if (monthList.isNullOrEmpty()) {
                binding.tvTotalExpensesValue.text = "No data"
                binding.rvReports.visibility = View.GONE
                binding.spinnerReportMonth.visibility = View.GONE
            } else {
                val monthNames = monthList.map { getMonthName(it) }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, monthNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerReportMonth.adapter = adapter

                // Spinner listener
                binding.spinnerReportMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        val selectedMonthInt = monthList[position]
                        viewModel.getBudgetsByMonth(userId, selectedMonthInt)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
            }
        }

        viewModel.budgetLD.observe(viewLifecycleOwner) { budgets ->
            reportAdapter.updateData(budgets)

            val totalSpend = budgets.sumOf { it.budgetSpend }
            val totalBudget = budgets.sumOf { it.budget }

            binding.tvTotalExpensesValue.text = "IDR %,d / IDR %,d".format(totalSpend, totalBudget)
        }
    }

    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "Januari"
            2 -> "Februari"
            3 -> "Maret"
            4 -> "April"
            5 -> "Mei"
            6 -> "Juni"
            7 -> "Juli"
            8 -> "Agustus"
            9 -> "September"
            10 -> "Oktober"
            11 -> "November"
            12 -> "Desember"
            else -> "Bulan Tidak Valid"
        }
    }

}