package com.example.ajokos.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val budget: Int,
    val budgetLeft: Int,
    val budgetSpend: Int,
    val userId: Int,
    val month: Int,
    val year: Int
)
