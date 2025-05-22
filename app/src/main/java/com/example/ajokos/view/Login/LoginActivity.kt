package com.example.ajokos.view.Login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ajokos.R
import com.example.ajokos.databinding.ActivityLoginBinding
import com.example.ajokos.model.SessionManager
import com.example.ajokos.view.Main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        sessionManager = SessionManager(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        if (sessionManager.isLogin()) {
            // Sudah login, langsung ke MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            // Belum login, tampilkan LoginFragment
            setContentView(view)
        }
    }
}
