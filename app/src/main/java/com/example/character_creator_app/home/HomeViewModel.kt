package com.example.character_creator_app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.dao.CharacterDao
import data.local.entity.CharacterDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: CharacterDao
) : ViewModel() {

    val allCharacters = dao.getAllCharacters().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    fun deleteCharacter(character: CharacterDto) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCharacter(character)
        }
    }
}
