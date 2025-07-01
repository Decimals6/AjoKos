package com.example.ajokos.view.Main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.ajokos.databinding.BudgetCardBinding
import com.example.ajokos.view.Main.BudgetFragmentDirections
import com.example.ajokos.model.data.Budget

class BudgetAdapter(val budgetList:ArrayList<Budget>,
                    private val readonlyMode: Boolean = false)
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
        val budget = budgetList[position]
        holder.binding.tvBudgetName.text = budget.name
        holder.binding.tvBudgetAmount.text = "IDR ${budget.budget}"
        holder.binding.tvBudgetUsage.text = "Spent: ${budget.budgetSpend} || Left: ${budget.budgetLeft}"
        holder.binding.tvBudgetMonth.text = getMonthName(budget.month)

        // Tambahkan status (aktif atau lama)
        holder.binding.tvBudgetStatus.text = if (readonlyMode) "Status: Riwayat" else "Status: Aktif"

        // Hanya bisa klik kalau bukan readonly
        if (!readonlyMode) {
            holder.binding.budgetObject.setOnClickListener {
                val action = BudgetFragmentDirections.actionToEditBudgetFragment(budget.id)
                Navigation.findNavController(it).navigate(action)
            }
        } else {
            holder.binding.budgetObject.setOnClickListener(null)
        }
    }

    fun updateBudgetList(newTodoList: List<Budget>) {
        budgetList.clear()
        budgetList.addAll(newTodoList)
        notifyDataSetChanged()
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

}