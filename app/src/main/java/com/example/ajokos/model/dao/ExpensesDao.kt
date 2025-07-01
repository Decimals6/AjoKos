package com.example.ajokos.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ajokos.model.data.Expenses
import com.example.ajokos.model.data.ExpensesWithBudget

@Dao
interface ExpensesDao {

    @Insert
    fun insert(expense: Expenses)

    @Delete
    fun delete(expense: Expenses)

    // Semua pakai relasi ke Budget
    @Transaction
    @Query("SELECT * FROM Expenses WHERE userId = :userId ORDER BY date DESC")
    fun getExpensesWithBudget(userId: Int): List<ExpensesWithBudget>

    @Transaction
    @Query("SELECT * FROM Expenses WHERE budgetId = :budgetId ORDER BY date DESC")
    fun getExpensesWithBudgetByBudget(budgetId: Int): List<ExpensesWithBudget>

    // ✅ GANTI: getExpensesThisMonth pakai timestamp range
    @Transaction
    @Query("""
        SELECT * FROM Expenses 
        WHERE userId = :userId 
          AND date BETWEEN :startTimestamp AND :endTimestamp
        ORDER BY date DESC
    """)
    fun getExpensesWithBudgetThisMonth(
        userId: Int,
        startTimestamp: Long,
        endTimestamp: Long
    ): List<ExpensesWithBudget>


    @Transaction
    @Query("""
    SELECT * FROM Expenses 
    WHERE userId = :userId 
    AND strftime('%m-%Y', datetime(date / 1000, 'unixepoch', 'localtime')) = :monthYear 
    ORDER BY date DESC
    """)
    fun getExpensesWithBudgetByMonth(userId: Int, monthYear: String): List<ExpensesWithBudget>


    @Query("""
    SELECT DISTINCT strftime('%m-%Y', datetime(date / 1000, 'unixepoch', 'localtime')) as monthYear 
    FROM Expenses 
    WHERE userId = :userId 
    ORDER BY datetime(date / 1000, 'unixepoch', 'localtime') DESC
    """)
    fun getAvailableMonths(userId: Int): List<String>
}

