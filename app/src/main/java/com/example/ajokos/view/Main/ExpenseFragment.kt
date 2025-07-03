package com.example.ajokos.view.Main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ajokos.R
import com.example.ajokos.databinding.FragmentExpenseBinding
import com.example.ajokos.model.SessionManager
import com.example.ajokos.viewmodel.ExpensesViewModel
import com.example.ajokos.viewmodel.UserViewModel

class ExpenseFragment : Fragment() {
    private lateinit var binding: FragmentExpenseBinding
    private lateinit var viewModel: ExpensesViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var expensesAdapter: ExpensesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel = ViewModelProvider(this)[ExpensesViewModel::class.java]
        val sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        // Inisialisasi adapter sekarang (saat context sudah tersedia)
        expensesAdapter = ExpensesAdapter(requireContext(), arrayListOf())

        // RecyclerView setup
        binding.rvExpenses.layoutManager = LinearLayoutManager(context)
        binding.rvExpenses.adapter = expensesAdapter

        // Fetch data
        viewModel.getExpensesThisMonth(userId)

        binding.fabAddExpense.setOnClickListener {
            val action = ExpenseFragmentDirections.actionToAddExpensesFragment()
            Navigation.findNavController(it).navigate(action)
        }

        binding.btnHistoryExpenses.setOnClickListener {
            val action = ExpenseFragmentDirections.actionToHistoryExpensesFragment()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()
    }
    private fun observeViewModel() {
        viewModel.expensesLD.observe(viewLifecycleOwner) {
            expensesAdapter.updateExpensesList(it)
        }
    }
}