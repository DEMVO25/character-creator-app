package com.example.character_creator_app.nav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.character_creator_app.home.Home
import com.example.character_creator_app.sign_in.SignIn
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow<Any>(SignIn)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {

            if (auth.currentUser != null) {
                _startDestination.value = Home
            } else {
                _startDestination.value = SignIn
            }
            _isLoading.value = false
        }
    }
}