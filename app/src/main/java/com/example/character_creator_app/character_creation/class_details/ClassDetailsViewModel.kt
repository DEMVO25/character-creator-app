package com.example.character_creator_app.character_creation.class_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.models.ClassDto
import data.remote.ApiService
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
    @Named("Open5e") private val open5eService: ApiService
) : ViewModel() {

    var uiState by mutableStateOf<ClassDetailsUiState>(ClassDetailsUiState())
        private set

    fun fetchClassDetails(className: String) {
        val classSlug = className.replace(" ", "-").lowercase()

        if (uiState.classDetails?.slug == classSlug) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val response = open5eService.getClassDetails(classSlug)
                if (response.isSuccessful) {
                    uiState = uiState.copy(classDetails = response.body(), isLoading = false)
                } else {
                    uiState =
                        uiState.copy(errorMessage = "Error: ${response.code()}", isLoading = false)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.localizedMessage, isLoading = false)
            }
        }
    }
}