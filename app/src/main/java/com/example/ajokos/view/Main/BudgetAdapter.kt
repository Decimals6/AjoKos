package com.example.ajokos.view.Main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.ajokos.databinding.BudgetCardBinding
import com.example.ajokos.view.Main.BudgetFragmentDirections
import com.example.ajokos.model.data.Budget

class BudgetAdapter(val budgetList:ArrayList<Budget>)
    : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {
    class BudgetViewHolder(var binding: BudgetCardBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        var binding = BudgetCardBinding.inflate(
            LayoutInflater.from(parent.context), parent,false)
        return BudgetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return budgetList.size
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.binding.tvBudgetName.text = budgetList[position].name
        holder.binding.tvBudgetAmount.text = "IDR " + budgetList[position].budget.toString()
        holder.binding.budgetObject.setOnClickListener {
            val action =
                BudgetFragmentDirections.actionToEditBudgetFragment(budgetList[position].id)

            Navigation.findNavController(it).navigate(action)
        }
    }
    fun updateBudgetList(newTodoList: List<Budget>) {
        budgetList.clear()
        budgetList.addAll(newTodoList)
        notifyDataSetChanged()
    }
}