package com.example.character_creator_app.sign_in

import androidx.lifecycle.ViewModel
import com.example.character_creator_app.R
import com.example.character_creator_app.UiText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _errorMessage = kotlinx.coroutines.flow.MutableStateFlow<UiText?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun signIn(email: String, pass: String, onSuccess: () -> Unit) {
        if (email.isBlank() || pass.length < 6) return

        _errorMessage.value = null

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val errorMsg = task.exception?.localizedMessage
                    _errorMessage.value = if (errorMsg != null) {
                        UiText.DynamicString(errorMsg)
                    } else {
                        UiText.StringResource(R.string.error_login_failed)
                    }
                }
            }
    }


    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _errorMessage.value = UiText.StringResource(R.string.empty_email_password_reset)
            return
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _errorMessage.value = UiText.StringResource(
                        R.string.password_reset_success, email
                    )
                } else {
                    val error = task.exception

                    _errorMessage.value = when (error) {
                        is FirebaseAuthInvalidUserException -> {
                            UiText.StringResource(R.string.user_not_found)
                        }

                        else -> {
                            UiText.DynamicString(error?.localizedMessage)
                        }
                    }
                }
            }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
