package ua.foxminded.auth.models

interface AuthErrorsRepository {
    fun getError(exception: Exception?): AuthError
}