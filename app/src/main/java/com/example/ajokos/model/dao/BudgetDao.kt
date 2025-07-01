package com.example.ajokos.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ajokos.model.data.Budget

@Dao
interface BudgetDao {
    @Insert
    fun insert(budget: Budget)

    @Query("UPDATE Budget SET name=:vname, budget=:vbudget, budgetLeft=:vbudgetLeft, budgetSpend=:vbudgetSpend WHERE id = :vid")
    fun update(vname: String, vbudget: Int, vbudgetLeft: Int, vbudgetSpend: Int, vid: Int)

    @Query("SELECT * FROM Budget WHERE id= :id")
    fun selectBudget(id:Int): Budget

    @Delete
    fun delete(budget: Budget)

    @Query("SELECT * FROM Budget WHERE userId = :userId")
    fun getAllBudgets(userId: Int): List<Budget>

    @Query("SELECT * FROM Budget WHERE userId = :userId AND month = :month AND year = :year")
    fun getBudgetsThisMonth(userId: Int, month: Int, year: Int): List<Budget>

    @Query("SELECT * FROM Budget WHERE userId = :userId AND month = :month")
    fun getBudgetByMonth(userId: Int, month: Int): List<Budget>

    @Query("SELECT DISTINCT month FROM Budget WHERE userId = :userId ORDER BY month DESC")
    fun getAvailableMonths(userId: Int): List<Int>

}
