package com.example.ajokos.model.dao

import androidx.room.*
import com.example.ajokos.model.data.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    fun login(username: String, password: String): User?

    @Query("SELECT * FROM User WHERE id = :userId")
    fun getUserById(userId: Int): User

    @Query("SELECT * FROM User WHERE username = :Username")
    fun checkUser(Username: String): User
}
