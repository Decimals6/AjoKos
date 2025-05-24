package com.example.ajokos.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ajokos.model.data.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    fun login(username: String, password: String): User

    @Query("SELECT * FROM user WHERE id = :userId")
    fun getUserById(userId: Int): User

    @Query("UPDATE User SET password=:vpass WHERE id = :vid")
    fun changePassword(vpass: String, vid: Int)

    @Query("SELECT * FROM user WHERE username = :username")
    fun checkUser(username: String): User
}

