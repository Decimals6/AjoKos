package com.example.ajokos.model.dao

import androidx.room.*
import com.example.ajokos.model.data.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): User?

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun checkUser(username: String): User?
}

