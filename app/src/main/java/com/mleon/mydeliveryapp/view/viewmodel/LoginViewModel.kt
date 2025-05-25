package com.mleon.mydeliveryapp.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns

class LoginViewModel : ViewModel() {
    private val _isEmailValid = MutableLiveData(false)
    private val _isPasswordValid = MutableLiveData(false)
    private val _isFormValid = MutableLiveData(false)

    val isEmailValid: LiveData<Boolean> = _isEmailValid
    val isPasswordValid: LiveData<Boolean> = _isPasswordValid
    val isFormValid: LiveData<Boolean> = _isFormValid

    fun onEmailChanged(email: String) {
        _isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        updateFormValid()
    }

    fun onPasswordChanged(password: String) {
        _isPasswordValid.value = password.length in 8..12
        updateFormValid()
    }

    private fun updateFormValid() {
        _isFormValid.value = (_isEmailValid.value == true && _isPasswordValid.value == true)
    }
}