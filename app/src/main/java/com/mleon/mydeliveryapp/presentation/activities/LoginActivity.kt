package com.mleon.mydeliveryapp.presentation.activities

import BottomSheetDialogFragment
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mleon.mydeliveryapp.R.*
import androidx.core.content.edit
import com.mleon.mydeliveryapp.databinding.ActivityLoginBinding
import android.text.TextWatcher
import android.text.Editable

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isEmailValid = false
    private var isPasswordValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val etEmailAddress = binding.etEmailAddress
        val etPassword = binding.etTextPassword
        val btnLogin = binding.btnLogin

        val sharedPref = getSharedPreferences("preferencias", MODE_PRIVATE)
        if (sharedPref.getString("usuariologueado", "false") == "true") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnLogin.isEnabled = false

        btnLogin.setOnClickListener {
            val email = etEmailAddress.text.toString()
            val password = etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                sharedPref.edit() {
                    putString("usuariologueado", "true")
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmailAddress.error = "El e-mail es inválido"
                } else if (password.isEmpty()) {
                    etPassword.error = "La contraseña está vacía"
                }
            }
        }

        etEmailAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                etEmailAddress.error = if (!isEmailValid) "El e-mail es inválido" else null
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                isPasswordValid = password.length in 8..12
                etPassword.error = when {
                    password.isEmpty() -> "La contraseña está vacía"
                    !isPasswordValid -> "La contraseña debe tener entre 8 y 12 caracteres"
                    else -> null
                }
                updateButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        val tvRegister = binding.tvRegister

        tvRegister.setOnClickListener {
            BottomSheetDialogFragment().show(supportFragmentManager, "BottomSheetDialogFragment")
        }

    }

    fun updateButtonState() {
        binding.btnLogin.isEnabled = isEmailValid && isPasswordValid
    }
}

