package com.example.ajokos.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fname: String,
    val lname: String,
    val username: String,
    val name: String,
    val password: String
)