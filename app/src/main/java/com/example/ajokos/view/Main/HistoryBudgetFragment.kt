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
import com.example.ajokos.databinding.FragmentAddEditBudgetBinding
import com.example.ajokos.databinding.FragmentBudgetBinding
import com.example.ajokos.databinding.FragmentHistoryBudgetBinding
import com.example.ajokos.model.SessionManager
import com.example.ajokos.model.data.Budget
import com.example.ajokos.viewmodel.BudgetViewModel
import com.example.ajokos.viewmodel.UserViewModel
import java.util.Calendar

class HistoryBudgetFragment : Fragment() {
    private lateinit var binding : FragmentHistoryBudgetBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var viewModel: BudgetViewModel
    private val budgetListAdapter = BudgetAdapter(arrayListOf(), readonlyMode = true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        val sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        viewModel.fetchAvailableMonths(userId)
        // RecyclerView setup
        binding.recviewHistoryBudget.layoutManager = LinearLayoutManager(context)
        binding.recviewHistoryBudget.adapter = budgetListAdapter

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.budgetLD.observe(viewLifecycleOwner) {
            budgetListAdapter.updateBudgetList(it)
            if (it.isEmpty()) {
                binding.recviewHistoryBudget.visibility = View.GONE
                binding.txtHistoryError.visibility = View.VISIBLE
            } else {
                binding.recviewHistoryBudget.visibility = View.VISIBLE
                binding.txtHistoryError.visibility = View.GONE
            }
        }

        viewModel.availableMonthsLD.observe(viewLifecycleOwner) { monthList ->
            if (monthList.isNullOrEmpty()) {
                binding.txtHistoryError.visibility = View.VISIBLE
                binding.spinnerMonth.visibility = View.GONE
                binding.recviewHistoryBudget.visibility = View.GONE
            } else {
                val monthNames = monthList.map { getMonthName(it) }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, monthNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerMonth.adapter = adapter

                // Spinner listener
                binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        val sessionManager = SessionManager(requireContext())
                        val userId = sessionManager.getUserId()
                        val selectedMonth = monthList[position]
                        viewModel.getBudgetsByMonth(userId, selectedMonth)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
            }
        }
    }
    fun getMonthName(month: Int): String {
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryBudgetFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}