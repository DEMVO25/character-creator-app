package com.example.character_creator_app.register

import androidx.lifecycle.ViewModel
import com.example.character_creator_app.R
import com.example.character_creator_app.UiText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _errorState = kotlinx.coroutines.flow.MutableStateFlow<UiText?>(null)
    val errorState = _errorState.asStateFlow()

    fun signUp(email: String, pass: String, username: String, onSuccess: () -> Unit) {
        _errorState.value = null

        if (email.isBlank() || username.isBlank()) {
            _errorState.value = UiText.StringResource(R.string.error_fill_all_fields)
            return
        }
        if (pass.length < 6) {
            _errorState.value = UiText.StringResource(R.string.error_password_too_short)
            return
        }

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    val user = hashMapOf(
                        "uid" to uid,
                        "username" to username,
                        "email" to email,
                        "role" to "player",
                        "createdAt" to com.google.firebase.Timestamp.now()
                    )

                    uid?.let { id ->
                        db.collection("users").document(id)
                            .set(user)
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                _errorState.value = UiText.StringResource(
                                    R.string.error_saving_data,
                                    e.localizedMessage
                                )
                            }
                    } ?: run {
                        _errorState.value = UiText.StringResource(R.string.error_registration_failed)
                    }
                } else {
                    val firebaseError = task.exception?.localizedMessage
                    _errorState.value = if (firebaseError != null) {
                        UiText.DynamicString(firebaseError)
                    } else {
                        UiText.StringResource(R.string.error_registration_failed)
                    }
                }
            }
    }

    fun clearError() {
        _errorState.value = null
    }
}