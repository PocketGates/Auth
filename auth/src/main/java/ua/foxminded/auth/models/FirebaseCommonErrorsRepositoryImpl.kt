package ua.foxminded.auth.models

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import ua.foxminded.auth.R

object FirebaseCommonErrorsRepositoryImpl : AuthErrorsRepository {

    override fun getError(exception: Exception?): AuthError {
        return when (exception) {
            is FirebaseAuthInvalidUserException -> AuthError(
                message = R.string.no_reset_user,
                userNameError = true
            )
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
            is FirebaseNetworkException -> AuthError(
                message = R.string.firebase_network_error
            )
            is FirebaseTooManyRequestsException -> AuthError(
                message = R.string.firebase_too_many_requests
            )
            else -> AuthError(
                message = R.string.something_is_wrong
            )
        }
    }
}