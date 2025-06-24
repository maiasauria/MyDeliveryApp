package com.mleon.core.model

/**
 * Represents a user in the application.
 *
 * @property email The user's email address.
 * @property name The user's first name.
 * @property lastname The user's last name.
 * @property address The user's address, which can be null if not provided.
 * @property userImageUrl The URL of the user's profile image, which can be null if not provided.
 * There is no password field in this model for security reasons, as passwords should not be stored in the model.
 */

data class User(
    val email: String,
    val name: String,
    val lastname: String,
    val address: String?,
    val userImageUrl: String? = null,
)