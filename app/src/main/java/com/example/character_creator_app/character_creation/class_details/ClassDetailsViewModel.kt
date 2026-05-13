package com.example.character_creator_app.character_creation.class_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.models.ClassDto
import data.remote.ApiService
import data.repository.ClassRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named



data class ClassDetailsUiState(
    val classDetails: ClassDto? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ClassDetailsViewModel @Inject constructor(
    private val repository: ClassRepository
) : ViewModel() {

    var uiState by mutableStateOf(ClassDetailsUiState())
        private set

    fun fetchClassDetails(className: String) {
        val classSlug = className.replace(" ", "-").lowercase()

        if (uiState.classDetails?.slug == classSlug) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val details = repository.getClassDetails(classSlug)
                if (details != null) {
                    uiState = uiState.copy(classDetails = details, isLoading = false)
                } else {
                    uiState = uiState.copy(errorMessage = "Class not found", isLoading = false)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.localizedMessage, isLoading = false)
            }
        }
    }
}