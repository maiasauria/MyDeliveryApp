package com.mleon.mydeliveryapp.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns

class RegisterViewModel : ViewModel() {

    private val _isEmailValid = MutableLiveData(false)
    val isEmailValid: LiveData<Boolean> = _isEmailValid

    private val _isNameValid = MutableLiveData(false)
    val isNameValid: LiveData<Boolean> = _isNameValid

    private val _isPasswordValid = MutableLiveData(false)
    val isPasswordValid: LiveData<Boolean> = _isPasswordValid

    private val _isConfirmValid = MutableLiveData(false)
    val isConfirmValid: LiveData<Boolean> = _isConfirmValid

    private val _isFormValid = MutableLiveData(false)
    val isFormValid: LiveData<Boolean> = _isFormValid

    private var password: String = ""

    fun onEmailChanged(email: String) {
        _isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        updateFormValid()
    }

    fun onNameChanged(name: String) {
        _isNameValid.value = name.trim().length in 6..50
        updateFormValid()
    }

    fun onPasswordChanged(pass: String) {
        password = pass
        _isPasswordValid.value = pass.length in 8..12
        updateFormValid()
    }

    fun onConfirmChanged(confirm: String) {
        _isConfirmValid.value = confirm.isNotEmpty() && confirm == password
        updateFormValid()
    }

    private fun updateFormValid() {
        _isFormValid.value = (_isEmailValid.value == true
                && _isNameValid.value == true
                && _isPasswordValid.value == true
                && _isConfirmValid.value == true)
    }
}