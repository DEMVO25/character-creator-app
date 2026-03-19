package com.example.character_creator_app.character_creation.character_languages

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.remote.ApiService
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LanguagesOptionViewModel @Inject constructor(
    @Named("DndApi") private val apiService: ApiService
) : ViewModel() {
    val languages = mutableStateListOf<data.models.LanguagesEntity>()
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun fetchLanguages() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {

                val response = apiService.getAllLanguages()
                languages.clear()
                languages.addAll(response.results)
            } catch (e: Exception) {
                errorMessage.value = "Failed to load languages: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }
}