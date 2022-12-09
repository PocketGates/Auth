package ua.foxminded.auth.models

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import ua.foxminded.auth.R

class SignUpErrorUseCase(private val errorsRepository: AuthErrorsRepository) {

    operator fun invoke(exception: Exception?): AuthError {
        return when (exception) {
            is FirebaseAuthWeakPasswordException -> AuthError(
                message = R.string.password_is_weak,
                passwordError = true
            )
            is FirebaseAuthInvalidCredentialsException -> AuthError(
                message = R.string.username_is_invalid,
                userNameError = true
            )
            is FirebaseAuthUserCollisionException -> AuthError(
                message = R.string.username_is_taken,
                userNameError = true
            )
            else -> errorsRepository.getError(exception)
        }
    }
}
