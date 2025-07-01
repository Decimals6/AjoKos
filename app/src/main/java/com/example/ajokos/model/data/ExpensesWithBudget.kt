package com.example.ajokos.model.data

import androidx.room.Embedded
import androidx.room.Relation

data class ExpensesWithBudget(
    @Embedded val expense: Expenses,

    @Relation(
        parentColumn = "budgetId",
        entityColumn = "id"
    )
    val budget: Budget
)