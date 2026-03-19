package com.example.character_creator_app.home


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import data.local.entity.CharacterDto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.ui.res.stringResource
import com.example.character_creator_app.R

@Composable
fun HomeScreenRoute(
    onNavigateToCreations: () -> Unit,
    onCharacterClick: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val characters by viewModel.allCharacters.collectAsStateWithLifecycle(initialValue = emptyList())

    HomeScreen(
        onNavigateToCreations = onNavigateToCreations,
        characters = characters,
        onCharacterClick = onCharacterClick,
        onDeleteCharacter = { viewModel.deleteCharacter(it) }
    )
}

@Composable
private fun HomeScreen(
    characters: List<CharacterDto>,
    onCharacterClick: (Int) -> Unit,
    onNavigateToCreations: () -> Unit,
    onDeleteCharacter: (CharacterDto) -> Unit
) {
    var characterToDelete by remember { mutableStateOf<CharacterDto?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreations) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.home_new_character_desc)
                )
            }
        }
    ) { padding ->

        characterToDelete?.let { character ->
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { characterToDelete = null },
                title = { Text(stringResource(R.string.home_delete_title)) },
                text = {
                    Text(stringResource(R.string.home_delete_confirm, character.name))
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            onDeleteCharacter(character)
                            characterToDelete = null
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.home_delete_action),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { characterToDelete = null }) {
                        Text(stringResource(R.string.home_cancel_action))
                    }
                }
            )
        }

        if (characters.isEmpty()) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.home_welcome_title),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(24.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(characters, key = { it.id }) { character ->
                    CharacterCard(
                        character = character,
                        onClick = { onCharacterClick(character.id) },
                        onDeleteClick = { characterToDelete = character }
                    )
                }
            }
        }
    }
}

@Composable
fun CharacterCard(
    character: CharacterDto,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .padding(start = 20.dp, end = 8.dp, top = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = character.name.ifEmpty { stringResource(R.string.home_unnamed_hero) },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.home_char_info_format, character.race, character.characterClass),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = stringResource(R.string.home_level_format, character.level),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            IconButton(
                onClick = onDeleteClick,
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.home_delete_icon_desc)
                )
            }
        }
    }
}