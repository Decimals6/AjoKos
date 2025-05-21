package com.example.ajokos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ajokos.model.data.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    fun login(username: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                repository.login(username, password)
            }
            onResult(user)
        }
    }

    fun register(user: User, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val existingUser = repository.getUserByUsername(user.username)
            if (existingUser != null) {
                onResult(false, "Username sudah digunakan")
            } else {
                repository.register(user)
                onResult(true, "Berhasil mendaftar")
            }
        }
    }
}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
