package com.example.ajokos.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ajokos.model.data.Expenses

@Dao
interface ExpensesDao {
    @Insert
    suspend fun insert(expense: Expenses)

    @Delete
    suspend fun delete(expense: Expenses)

    @Query("SELECT * FROM Expenses WHERE userId = :userId ORDER BY date DESC")
    suspend fun getExpensesByUser(userId: Int): LiveData<List<Expenses>>

    @Query("SELECT * FROM Expenses WHERE budgetId = :budgetId ORDER BY date DESC")
    suspend fun getExpensesByBudget(budgetId: Int): LiveData<List<Expenses>>
}
