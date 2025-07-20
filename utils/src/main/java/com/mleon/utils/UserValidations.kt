package com.mleon.utils

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

object UserValidations {
    private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    private const val PASSWORD_MIN_LENGTH = 8
    private const val PASSWORD_MAX_LENGTH = 12
    private const val NAME_MIN_LENGTH = 2
    private const val NAME_MAX_LENGTH = 20

    // Error message constants
    const val ERROR_NAME = "El nombre no puede estar vacío"
    const val ERROR_LASTNAME = "El apellido no puede estar vacío"
    const val ERROR_EMAIL = "El email no es válido"
    const val ERROR_PASSWORD = "La contraseña debe tener entre 8 y 12 caracteres"
    const val ERROR_PASSWORD_CONFIRM = "Las contraseñas no coinciden"

    fun validateName(name: String): ValidationResult =
        if (name.isBlank() || name.length !in NAME_MIN_LENGTH..NAME_MAX_LENGTH)
            ValidationResult(false, ERROR_NAME)
        else
            ValidationResult(true)

    fun validateLastname(lastname: String): ValidationResult =
        if (lastname.isBlank() || lastname.length !in NAME_MIN_LENGTH..NAME_MAX_LENGTH)
            ValidationResult(false, ERROR_LASTNAME)
        else
            ValidationResult(true)

    fun validateEmail(email: String): ValidationResult =
        if (!Regex(EMAIL_REGEX).matches(email))
            ValidationResult(false, ERROR_EMAIL)
        else
            ValidationResult(true)

    fun validatePassword(password: String): ValidationResult =
        if (password.length !in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH)
            ValidationResult(false, ERROR_PASSWORD)
        else
            ValidationResult(true)

    fun validatePasswordConfirm(password: String, confirm: String): ValidationResult =
        if (password != confirm || confirm.isBlank())
            ValidationResult(false, ERROR_PASSWORD_CONFIRM)
        else
            ValidationResult(true)
}