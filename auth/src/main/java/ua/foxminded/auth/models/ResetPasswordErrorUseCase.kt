package ua.foxminded.auth.models

import com.google.firebase.auth.FirebaseAuthInvalidUserException
import ua.foxminded.auth.R

class ResetPasswordErrorUseCase(private val errorsRepository: AuthErrorsRepository) {

    operator fun invoke(exception: Exception?): AuthError {
        return when (exception) {
            is FirebaseAuthInvalidUserException -> AuthError(
                message = R.string.no_reset_user,
                userNameError = true
            )
            else -> errorsRepository.getError(exception)
        }
    }
}