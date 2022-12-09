package ua.foxminded.auth.ui

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.foxminded.auth.R
import ua.foxminded.auth.models.AuthError
import ua.foxminded.auth.models.ResetPasswordErrorUseCase
import ua.foxminded.auth.models.SignInErrorUseCase
import ua.foxminded.auth.models.SignUpErrorUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val resetPasswordErrorUseCase: ResetPasswordErrorUseCase,
    private val signUpErrorUseCase: SignUpErrorUseCase,
    private val signInErrorUseCase: SignInErrorUseCase
) : ViewModel() {
    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState = _authUiState.asStateFlow()

    private var auth: FirebaseAuth? = null

    fun onCreate() {
        auth = Firebase.auth
    }

    fun send(event: AuthEvent) {
        when (event) {
            is SignUpEvent -> {
                signUp(event.userName, event.password)
            }
            is SignInEvent -> {
                signIn(event.userName, event.password)
            }
            is ResetPasswordEvent -> {
                resetPassword(event.userName)
            }
        }
    }

    private fun resetPassword(userName: String) {
        if (userName.isEmpty()) {
            setErrorMessage(AuthError(message = R.string.user_name_is_invalid))
            setPasswordResetError()
            return
        }
        auth?.sendPasswordResetEmail(userName)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                setPasswordResetStatus()
            } else {
                setErrorMessage(resetPasswordErrorUseCase.invoke(task.exception))
            }
        }
    }

    private fun setPasswordResetStatus() {
        _authUiState.value =
            _authUiState.value.copy(passwordResetStatus = true, passwordResetError = false)
    }

    private fun setPasswordResetError() {
        _authUiState.value = _authUiState.value.copy(passwordResetError = true)
    }

    private fun signIn(userName: String, password: String) {
        setLoading(true)
        auth?.signInWithEmailAndPassword(userName, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setLogged()
                } else {
                    setErrorMessage(signInErrorUseCase.invoke(task.exception))
                }
                setLoading(false)
            }
    }

    private fun signUp(userName: String, password: String) {
        setLoading(true)
        auth?.createUserWithEmailAndPassword(userName, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setLogged()
                } else {
                    setErrorMessage(signUpErrorUseCase.invoke(task.exception))
                }
                setLoading(false)
            }
    }


    fun resetUiState() {
        _authUiState.value = AuthUiState()
    }

    private fun setLogged() {
        _authUiState.value = _authUiState.value.copy(isLogged = true)
    }

    private fun setLoading(isLoading: Boolean) {
        _authUiState.value = _authUiState.value.copy(isLoading = isLoading)
    }

    private fun setErrorMessage(error: AuthError) {
        _authUiState.value = _authUiState.value.copy(
            errorMessage = error.message,
            userNameError = error.userNameError,
            passwordError = error.passwordError
        )
    }
}

