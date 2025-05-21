package com.mleon.mydeliveryapp.presentation.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mleon.mydeliveryapp.R
import com.mleon.mydeliveryapp.R.*
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout.activity_login)

        val editTextTextEmailAddress = findViewById<EditText>(id.editTextTextEmailAddress)
        val editTextTextPassword = findViewById<EditText>(id.editTextTextPassword)
        val btnLogin = findViewById<Button>(id.btnLogin)

        val sharedPref = getSharedPreferences("my_prefs", MODE_PRIVATE)

        btnLogin.setOnClickListener {
            val email = editTextTextEmailAddress.text.toString()
            val password = editTextTextPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                sharedPref.edit() {
                    putString("usuariologueado", "true")
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // Mostrar un mensaje de error
            }
        }

    }
}