package com.example.character_creator_app.character_creation.skills

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
class SkillsViewModel @Inject constructor(
    @Named("DndApi") private val apiService: ApiService
) : ViewModel() {
    val skills = mutableStateListOf<data.models.SkillsEntity>()
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    init {
        fetchSkills()
    }

    fun fetchSkills() {
        if (skills.isNotEmpty()) return
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val response = apiService.getAllSkills()
                skills.clear()
                skills.addAll(response.results)
            } catch (e: Exception) {
                errorMessage.value = "Failed to load skills: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }
}