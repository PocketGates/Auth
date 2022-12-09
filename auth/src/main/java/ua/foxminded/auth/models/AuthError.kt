package ua.foxminded.auth.models

data class AuthError(
    val message: Int,
    val userNameError: Boolean = false,
    val passwordError: Boolean = false
)
