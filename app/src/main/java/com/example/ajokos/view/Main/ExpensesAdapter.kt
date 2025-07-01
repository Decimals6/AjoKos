package com.example.ajokos.view.Main

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ajokos.databinding.DialogExpenseDetailBinding
import com.example.ajokos.databinding.ExpensesCardBinding
import com.example.ajokos.model.data.ExpensesWithBudget
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpensesAdapter(
    private val context: Context,
    private val expensesList: ArrayList<ExpensesWithBudget>
) : RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder>() {

    inner class ExpensesViewHolder(val binding: ExpensesCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ExpensesCardBinding.inflate(inflater, parent, false)
        return ExpensesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        val expenseWithBudget = expensesList[position]
        val expense = expenseWithBudget.expense
        val budget = expenseWithBudget.budget

        // Format tanggal pendek
        val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale("id")).format(expense.date)
        holder.binding.txtExpenseDate.text = formattedDate

        // Nama Budget pakai Chip
        holder.binding.chipBudgetName.text = budget.name

        // Nominal
        holder.binding.txtExpenseAmount.text = expense.nominal.toString()

        // Klik item untuk tampilkan dialog detail
        holder.binding.root.setOnClickListener {
            showExpenseDetailDialog(expenseWithBudget)
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id"))
        Log.d("DEBUG_DATE", "timestamp = ${expense.date}, formatted = ${sdf.format(Date(expense.date))}")


    }

    override fun getItemCount(): Int = expensesList.size

    fun updateExpensesList(newList: List<ExpensesWithBudget>) {
        expensesList.clear()
        expensesList.addAll(newList)
        notifyDataSetChanged()
    }

    // Fungsi untuk menampilkan dialog detail pakai ViewBinding
    private fun showExpenseDetailDialog(expenseWithBudget: ExpensesWithBudget) {
        val dialogBinding = DialogExpenseDetailBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context).setView(dialogBinding.root).create()

        val expense = expenseWithBudget.expense
        val budget = expenseWithBudget.budget

        // Format tanggal lengkap
        val fullDate = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id")).format(expense.date)
        dialogBinding.txtDialogDate.text = fullDate
        dialogBinding.txtDialogAmount.text = "IDR ${expense.nominal}"
        dialogBinding.txtDialogDescription.text = expense.description
        dialogBinding.chipDialogBudgetName.text = budget.name

        dialogBinding.btnCloseDialog.setOnClickListener { dialog.dismiss() }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}
