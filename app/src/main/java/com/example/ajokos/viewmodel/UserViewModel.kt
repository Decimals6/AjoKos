package com.example.ajokos.viewmodel

import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ajokos.model.SessionManager
import com.example.ajokos.model.data.User
import com.example.ajokos.model.database.AppDatabase
import com.example.ajokos.view.Login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class UserViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

//register
    private val userDao = AppDatabase.getDatabase(application).userDao()
    private val sessionManager = SessionManager(application)

    private val _signupResult = MutableLiveData<String>()
    val signupResult: LiveData<String> = _signupResult

//login
    private val _loginResult = MutableLiveData<String?>()
    val loginResult: LiveData<String?> = _loginResult

    private val _loggedInUserId = MutableLiveData<Int?>()
    val loggedInUserId: LiveData<Int?> = _loggedInUserId

    val selectedUser = MutableLiveData<User>()
    val paswordChangeMes = MutableLiveData<String>()

    private var job = Job()

    val oldPassword = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val repeatPassword = MutableLiveData<String>()

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

    private fun hashPassword(password: String): String {
        val bytes = java.security.MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun login(username: String, password: String) {
        launch() {
            val hashedPassword = hashPassword(password)
            val user = userDao.login(username, hashedPassword)
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

    //binding function
    fun onChangePasswordClicked() {
        launch {
            val userId = sessionManager.getUserId()
            val user = userDao.getUserById(userId)

            if (oldPassword.value.isNullOrBlank() || newPassword.value.isNullOrBlank() || repeatPassword.value.isNullOrBlank()) {
                showToast("Please fill all fields")
                return@launch
            }

            if (oldPassword.value != user.password) {
                showToast("Old password is incorrect")
                return@launch
            }

            if (newPassword.value != repeatPassword.value) {
                showToast("New passwords do not match")
                return@launch
            }

            userDao.changePassword(newPassword.value.toString(), user.id)
            paswordChangeMes.postValue("Password updated successfully") // gunakan postValue
            showToast("Password updated successfully")
            clearFields()
        }
    }

    fun onLogoutClicked() {
        sessionManager.logout()
        val context = getApplication<Application>().applicationContext
        val intent = Intent(context, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)
    }

    private fun showToast(message: String) {
        launch{
            withContext(Dispatchers.Main) {
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        oldPassword.postValue("")
        newPassword.postValue("")
        repeatPassword.postValue("")
    }
}
