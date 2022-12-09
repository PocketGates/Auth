package ua.foxminded.auth.models

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import ua.foxminded.auth.R

class SignInErrorUseCase(private val errorsRepository: AuthErrorsRepository) {
    operator fun invoke(exception: Exception?): AuthError {
        return when (exception) {
            is FirebaseAuthInvalidUserException -> AuthError(
                message = R.string.user_is_not_registered,
                userNameError = true
            )
            is FirebaseAuthInvalidCredentialsException -> AuthError(
                message = R.string.the_pass_is_wrong,
                userNameError = true
            )
            else -> errorsRepository.getError(exception)
        }
    }
}