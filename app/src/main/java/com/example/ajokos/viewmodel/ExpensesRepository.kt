package com.example.ajokos.viewmodel

import androidx.lifecycle.LiveData
import com.example.ajokos.model.dao.ExpensesDao
import com.example.ajokos.model.data.Expenses

class ExpensesRepository(private val expensesDao: ExpensesDao) {
    fun getByUser(userId: Int): LiveData<List<Expenses>> {
        return expensesDao.getExpensesByUser(userId)
    }
    fun getByBudget(budgetId: Int): LiveData<List<Expenses>> {
        return expensesDao.getExpensesByBudget(budgetId)
    }
    fun insert(expense: Expenses) {
        expensesDao.insert(expense)
    }
    fun delete(expense: Expenses) {
        expensesDao.delete(expense)
    }
}
