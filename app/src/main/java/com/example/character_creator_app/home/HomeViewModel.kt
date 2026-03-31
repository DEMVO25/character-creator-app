package com.example.character_creator_app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.dao.CharacterDao
import data.local.entity.CharacterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: CharacterDao
) : ViewModel() {

    data class HomeUiState(
        val characters: List<CharacterEntity> = emptyList(),
        val isLoading: Boolean = true,
        val errorMessage: String? = null
    )

    val uiState = dao.getAllCharacters()
        .map { characters ->
            HomeUiState(characters = characters, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isLoading = true)
        )

    fun deleteCharacter(character: CharacterEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCharacter(character)
        }
    }
}