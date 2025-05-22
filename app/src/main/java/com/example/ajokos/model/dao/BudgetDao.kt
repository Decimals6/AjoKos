package com.example.ajokos.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ajokos.model.data.Budget

@Dao
interface BudgetDao {
    @Insert
    fun insert(budget: Budget)

    @Update
    fun update(budget: Budget)

    @Delete
    fun delete(budget: Budget)

    @Query("SELECT * FROM Budget WHERE userId = :userId")
    fun getAllBudgets(userId: Int): LiveData<List<Budget>>
}
