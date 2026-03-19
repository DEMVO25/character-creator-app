package com.example.character_creator_app.character_creation.spell_sheet_creation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel

@Composable
fun SpellSheetCreationRoute(
    parentEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onNext: () -> Unit,

    ) {

    val sharedViewModel: SharedCharacterViewModel = hiltViewModel(parentEntry)

    SpellSheetCreationScreen(
        onBack = onBack,
        sharedViewModel = sharedViewModel,
        onNext = onNext,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpellSheetCreationScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    sharedViewModel: SharedCharacterViewModel
) {

    val characterState by sharedViewModel.characterState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.spells_cantrips_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_button_label))
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        sharedViewModel.saveCharacterToDb()
                        onNext()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.next_button_label))
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.spells_starting_magic_desc),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = characterState.cantrips,
                onValueChange = { newValue ->
                    sharedViewModel.updateSpellSheetCreation(newValue, characterState.lvl1spells)
                },
                label = { Text(stringResource(R.string.spells_cantrips_field_label)) },
                placeholder = { Text(stringResource(R.string.spells_cantrips_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Next
                ),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = characterState.lvl1spells,
                onValueChange = { newValue ->
                    sharedViewModel.updateSpellSheetCreation(characterState.cantrips, newValue)
                },
                label = { Text(stringResource(R.string.spells_lvl1_field_label)) },
                placeholder = { Text(stringResource(R.string.spells_lvl1_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                ),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

