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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class UserViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

//register
    private val userDao = AppDatabase.getDatabase(application).userDao()

    private val _signupResult = MutableLiveData<String>()
    val signupResult: LiveData<String> = _signupResult

//login
    private val _loginResult = MutableLiveData<String?>()
    val loginResult: LiveData<String?> = _loginResult

    private val _loggedInUserId = MutableLiveData<Int?>()
    val loggedInUserId: LiveData<Int?> = _loggedInUserId

    val selectedUser = MutableLiveData<User>()

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun registerUser(user: User) {
        launch() {
            val existingUser = userDao.checkUser(user.username)
            if (existingUser != null) {
                _signupResult.postValue("Username sudah terdaftar")
            } else {
                userDao.insert(user)
                _signupResult.postValue("Pendaftaran berhasil")
            }
        }
    }

    fun getUserData(id:Int){
        launch {
            val db = AppDatabase.getDatabase(getApplication())
            selectedUser.postValue(db.userDao().getUserById(id))
        }
    }



    fun login(username: String, password: String) {
        launch() {
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
