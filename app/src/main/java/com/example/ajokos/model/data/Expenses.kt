package com.example.ajokos.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expenses(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long, // Unix timestamp
    val budgetId: Int,
    val nominal: Int,
    val description: String,
    val userId: Int
)
