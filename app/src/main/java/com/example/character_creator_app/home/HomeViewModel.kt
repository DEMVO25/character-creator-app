package com.example.character_creator_app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.dao.CharacterDao
import data.local.entity.CharacterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: CharacterDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    private val _userName = MutableStateFlow<String?>(null)

    data class HomeUiState(
        val characters: List<CharacterEntity> = emptyList(),
        val isLoading: Boolean = true,
        val errorMessage: String? = null,
        val userName: String? = null
    )

    val uiState = combine(
        (currentUserId?.let { dao.getCharactersForUser(it) } ?: flowOf(emptyList())),
        _userName
    ) { characters, name ->
        HomeUiState(
            characters = characters,
            isLoading = false,
            userName = name ?: auth.currentUser?.email
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    init {
        fetchCharactersFromFirestore()
        fetchUserData()
    }

    private fun fetchUserData() {
        val uid = currentUserId ?: return

        firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val name = snapshot.getString("username")
                _userName.value = name
            }
    }

    private fun fetchCharactersFromFirestore() {
        val userId = currentUserId ?: return

        firestore.collection("characters")
            .whereEqualTo("ownerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e("Firestore", "Sync error", error)
                    return@addSnapshotListener
                }

                val remoteCharacters = snapshot?.toObjects(CharacterEntity::class.java) ?: return@addSnapshotListener

                viewModelScope.launch(Dispatchers.IO) {
                    remoteCharacters.forEach { character ->
                        dao.upsertCharacter(character)
                    }
                }
            }
    }

    fun deleteCharacter(character: CharacterEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCharacter(character)

            firestore.collection("characters")
                .whereEqualTo("ownerId", currentUserId)
                .whereEqualTo("id", character.id)
                .get()
                .addOnSuccessListener { docs ->
                    for (doc in docs) doc.reference.delete()
                }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}