package com.example.ajokos.view.Main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ajokos.databinding.ReportCardBinding
import com.example.ajokos.model.data.Budget
import java.text.NumberFormat
import java.util.Locale

class ReportAdapter(
    private val reportList: ArrayList<Budget>
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(val binding: ReportCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding = ReportCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val budget = reportList[position]

        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        holder.binding.tvReportBudgetName.text = budget.name
        holder.binding.tvReportBudgetLeft.text = formatter.format(budget.budgetSpend)
        holder.binding.tvReportBudgetTotal.text = formatter.format(budget.budget)
        holder.binding.tvReportBudgetRemaining.text =
            "Budget Left: ${formatter.format(budget.budgetLeft)}"

        // Set progress
        val progress = if (budget.budget != 0) {
            (budget.budgetSpend * 100 / budget.budget)
        } else 0

        holder.binding.progressReport.progress = progress
    }

    override fun getItemCount(): Int = reportList.size

    fun updateData(newList: List<Budget>) {
        reportList.clear()
        reportList.addAll(newList)
        notifyDataSetChanged()
    }
}
