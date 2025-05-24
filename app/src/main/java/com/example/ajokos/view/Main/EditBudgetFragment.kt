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
import com.example.ajokos.viewmodel.BudgetViewModel

class EditBudgetFragment : Fragment() {
    private lateinit var binding: FragmentAddEditBudgetBinding
    private lateinit var viewModel: BudgetViewModel

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

        // retrieve the argument from clicked+ imgEdit
        val id = EditBudgetFragmentArgs.fromBundle(requireArguments()).id
        viewModel.getBudgetById(id)
        observeViewModel()
        // pass the uuid argument to DetailViewModel (fetch function)
//        viewModel.(id)
//        observeViewModel()
//        binding.btnAdd.setOnClickListener {
//            val radio =
//                view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//            viewModel.update(binding.txtTitle.text.toString(),
//                binding.txtNote.text.toString(), radio.tag.toString().toInt(), uuid)
//            Toast.makeText(view.context, "Todo updated", Toast.LENGTH_SHORT).show()
//            Navigation.findNavController(it).popBackStack()
//        }

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

    }
    fun observeViewModel() {
        viewModel.selectedBudget.observe(viewLifecycleOwner, Observer {
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