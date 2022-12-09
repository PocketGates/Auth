package ua.foxminded.auth.ui

sealed class AuthEvent(var userName: String = "", var password: String = "") {
    fun handle(userName: String, password: String = "") {
        this.userName = userName
        this.password = password
    }
}

object SignInEvent : AuthEvent()
object SignUpEvent : AuthEvent()
object ResetPasswordEvent : AuthEvent()