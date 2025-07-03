package com.example.ajokos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.ajokos.model.data.Budget
import com.example.ajokos.model.data.Expenses
import com.example.ajokos.model.data.ExpensesWithBudget
import com.example.ajokos.model.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

class ExpensesViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val expensesDao = AppDatabase.getDatabase(application).expensesDao()
    val expensesLD = MutableLiveData<List<ExpensesWithBudget>>()
    val availableMonthsLD = MutableLiveData<List<String>>()
    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    // Fungsi hitung timestamp awal dan akhir bulan sekarang
    private fun getThisMonthRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()

        // Awal bulan
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val startOfMonth = cal.timeInMillis

        // Akhir bulan
        cal.add(Calendar.MONTH, 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.DATE, -1)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        val endOfMonth = cal.timeInMillis

        return Pair(startOfMonth, endOfMonth)
    }

    // GANTI ke versi timestamp
    fun getExpensesThisMonth(userId: Int) {
        launch {
            val (start, end) = getThisMonthRange()
            val result = expensesDao.getExpensesWithBudgetThisMonth(userId, start, end)
            expensesLD.postValue(result)
        }
    }

    fun getExpensesByMonth(userId: Int, monthStr: String) {
        launch {
            val result = expensesDao.getExpensesWithBudgetByMonth(userId, monthStr)
            expensesLD.postValue(result)
        }
    }

    fun fetchAvailableMonths(userId: Int) {
        launch {
            val result = expensesDao.getAvailableMonths(userId)
            availableMonthsLD.postValue(result)
        }
    }

    fun addExpenseAndUpdateBudget(expense: Expenses, budget: Budget) {
        viewModelScope.launch(Dispatchers.IO) {
            expensesDao.insert(expense)

            val updatedLeft = budget.budgetLeft - expense.nominal
            val updatedSpend = budget.budgetSpend + expense.nominal

            AppDatabase.getDatabase(getApplication())
                .budgetDao()
                .update(budget.name, budget.budget, updatedLeft, updatedSpend, budget.id)
        }
    }
}
