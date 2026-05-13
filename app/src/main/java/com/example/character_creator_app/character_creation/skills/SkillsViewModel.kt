package com.example.character_creator_app.character_creation.skills

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.remote.ApiService
import data.repository.SkillsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SkillsViewModel @Inject constructor(
    private val repository: SkillsRepository
) : ViewModel() {

    val skills = mutableStateListOf<data.models.SkillsEntity>()
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    init {
        fetchSkills()
    }

    fun fetchSkills() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val results = repository.getSkills()
                skills.clear()
                skills.addAll(results)
            } catch (e: Exception) {
                errorMessage.value = "Failed to load skills: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }
}