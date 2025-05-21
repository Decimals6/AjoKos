package com.example.ajokos.viewmodel

import com.example.ajokos.model.dao.UserDao
import com.example.ajokos.model.data.User

class UserRepository(private val userDao: UserDao) {
    fun login(username: String, password: String): User? {
        return userDao.login(username, password)
    }

    fun register(user: User) {
        userDao.insert(user)
    }

    fun getUserById(userId: Int): User {
        return userDao.getUserById(userId)
    }
    fun getUserByUsername(username: String): User? {
        return userDao.checkUser(username)
    }
}
