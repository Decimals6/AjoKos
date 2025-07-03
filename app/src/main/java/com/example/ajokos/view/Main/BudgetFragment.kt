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
import java.text.SimpleDateFormat
import java.util.Locale

class BudgetFragment : Fragment() {
    private lateinit var binding : FragmentBudgetBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var viewModel: BudgetViewModel
    private val budgetListAdapter = BudgetAdapter(arrayListOf(), readonlyMode = false)


    val currentDate = java.util.Calendar.getInstance()
    val formatter = SimpleDateFormat("MMMM", Locale("in", "ID"))
    val currentMonth = formatter.format(currentDate.time)

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
        val sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        // Panggil data budget bulan ini
        viewModel.getBudgetsThisMonth(userId)
        viewModel.fetchAvailableMonths(userId)

        // Atur layout dan adapter
        binding.recviewBudget.layoutManager = LinearLayoutManager(context)
        binding.recviewBudget.adapter = budgetListAdapter

        // Tombol tambah budget
        binding.fabAddBudget.setOnClickListener {
            val action = BudgetFragmentDirections.actionToAddBudgetFragment()
            Navigation.findNavController(it).navigate(action)
        }

        // Tombol lihat history
        binding.btnHistory.setOnClickListener {
            val action = BudgetFragmentDirections.actionToHistoryBudgetFragment()
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
                binding.txtError.setText("Welcome to $currentMonth, \nready to plan your budget this month?")
            } else {
                binding.recviewBudget?.visibility = View.VISIBLE
                binding.txtError.visibility = View.GONE
            }
        })
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            BudgetFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}