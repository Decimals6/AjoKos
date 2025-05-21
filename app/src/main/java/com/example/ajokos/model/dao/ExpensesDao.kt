package com.example.ajokos.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ajokos.model.data.Expenses

@Dao
interface ExpensesDao {
    @Insert
    fun insert(expense: Expenses)

    @Delete
    fun delete(expense: Expenses)

    @Query("SELECT * FROM Expenses WHERE userId = :userId ORDER BY date DESC")
    fun getExpensesByUser(userId: Int): LiveData<List<Expenses>>

    @Query("SELECT * FROM Expenses WHERE budgetId = :budgetId ORDER BY date DESC")
    fun getExpensesByBudget(budgetId: Int): LiveData<List<Expenses>>
}
