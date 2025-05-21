package com.example.ajokos.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ajokos.model.data.Budget

@Dao
interface BudgetDao {
    @Insert
    suspend fun insert(budget: Budget)

    @Update
    suspend fun update(budget: Budget)

    @Delete
    suspend fun delete(budget: Budget)

    @Query("SELECT * FROM Budget WHERE userId = :userId")
    suspend fun getAllBudgets(userId: Int): LiveData<List<Budget>>
}
