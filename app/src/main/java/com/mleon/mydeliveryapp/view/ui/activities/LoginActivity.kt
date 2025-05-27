package com.mleon.mydeliveryapp.view.ui.activities

import RegisterFragment
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.mleon.mydeliveryapp.databinding.ActivityLoginBinding
import android.text.TextWatcher
import android.text.Editable
import com.mleon.mydeliveryapp.view.viewmodel.LoginViewModel
import androidx.activity.viewModels


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("preferencias", MODE_PRIVATE)

        if (sharedPref.getString("usuariologueado", "false") == "true") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.isEnabled = false

        binding.btnLogin.setOnClickListener {
            sharedPref.edit() {
                putString("usuariologueado", "true")
                putString("email", binding.etEmailAddress.text.toString())
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        binding.etEmailAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onEmailChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onPasswordChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        viewModel.isEmailValid.observe(this) { isValid ->
            binding.etEmailAddress.error =
                if (!isValid && binding.etEmailAddress.text.isNotEmpty()) "El e-mail es inválido" else null
        }

        viewModel.isPasswordValid.observe(this) { isValid ->
            val password = binding.etPassword.text.toString()
            binding.etPassword.error = when {
                password.isEmpty() -> "La contraseña está vacía"
                !isValid -> "La contraseña debe tener entre 8 y 12 caracteres"
                else -> null
            }
        }
        viewModel.isFormValid.observe(this) { isValid ->
            binding.btnLogin.isEnabled = isValid
        }

        binding.tvRegister.setOnClickListener {
            RegisterFragment().show(supportFragmentManager, "BottomSheetDialogFragment")
        }

    }

}

