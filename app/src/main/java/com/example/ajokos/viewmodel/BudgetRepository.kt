package com.example.ajokos.viewmodel

import androidx.lifecycle.LiveData
import com.example.ajokos.model.dao.BudgetDao
import com.example.ajokos.model.data.Budget

class BudgetRepository(private val budgetDao: BudgetDao) {
    fun getAllBudgets(userId: Int): LiveData<List<Budget>> {
        return budgetDao.getAllBudgets(userId)
    }
    fun insert(budget: Budget) {
        budgetDao.insert(budget)
    }
    fun update(budget: Budget) {
        budgetDao.update(budget)
    }
    fun delete(budget: Budget) {
        budgetDao.delete(budget)
    }
}
