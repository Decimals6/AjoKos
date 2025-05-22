package com.example.ajokos.viewmodel

import android.app.Application
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ajokos.model.data.User
import com.example.ajokos.model.database.AppDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserViewModel(application: Application) : AndroidViewModel(application) {

//register
    private val userDao = AppDatabase.getDatabase(application).userDao()

    private val _signupResult = MutableLiveData<String>()
    val signupResult: LiveData<String> = _signupResult

//login
    private val _loginResult = MutableLiveData<String?>()
    val loginResult: LiveData<String?> = _loginResult

    private val _loggedInUserId = MutableLiveData<Int?>()
    val loggedInUserId: LiveData<Int?> = _loggedInUserId


    fun registerUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingUser = userDao.checkUser(user.username)
            if (existingUser != null) {
                _signupResult.postValue("Username sudah terdaftar")
            } else {
                userDao.insert(user)
                _signupResult.postValue("Pendaftaran berhasil")
            }
        }
    }


    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.login(username, password)
            if (user != null) {
                _loggedInUserId.postValue(user.id)
                _loginResult.postValue("Login berhasil")
            } else {
                _loginResult.postValue("Username atau password salah")
            }
        }
    }

    fun clearLoginResult() {
        _loginResult.value = null
    }
}
