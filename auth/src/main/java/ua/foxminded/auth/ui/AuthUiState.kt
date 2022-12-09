package ua.foxminded.auth.ui

data class AuthUiState(
    val userNameError: Boolean = false,
    val passwordError: Boolean = false,
    val passwordResetError: Boolean = false,
    val passwordResetStatus: Boolean = false,
    val isLoading: Boolean = false,
    val isLogged: Boolean = false,
    val errorMessage: Int? = null
)
