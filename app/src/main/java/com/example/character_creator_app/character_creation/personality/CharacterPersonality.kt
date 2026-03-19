package com.example.character_creator_app.character_creation.personality

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel


@Composable
fun PersonalityRoute(
    parentEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onNext: () -> Unit,

    ) {

    val sharedViewModel: SharedCharacterViewModel = hiltViewModel(parentEntry)

    Personality(
        onBack = onBack,
        sharedViewModel = sharedViewModel,
        onNext = onNext,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Personality(
    onBack: () -> Unit,
    onNext: () -> Unit,
    sharedViewModel: SharedCharacterViewModel,
) {
    val characterState by sharedViewModel.characterState.collectAsState()
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.character_personality_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                    onClick = onNext,
                    enabled = characterState.personalTraits.isNotBlank() &&
                            characterState.ideals.isNotBlank() &&
                            characterState.bonds.isNotBlank() &&
                            characterState.flaws.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.next_button_label))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            PersonalityField(
                label = stringResource(R.string.personality_traits_label),
                value = characterState.personalTraits,
                onValueChange = { newValue ->
                    sharedViewModel.updatePersonality(
                        personalTraits = newValue,
                        ideals = characterState.ideals,
                        bonds = characterState.bonds,
                        flaws = characterState.flaws
                    )
                },
                modifier = Modifier.height(120.dp),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Next
                ),
            )

            PersonalityField(
                label = stringResource(R.string.personality_ideals_label),
                value = characterState.ideals,
                onValueChange = { newValue ->
                    sharedViewModel.updatePersonality(
                        personalTraits = characterState.personalTraits,
                        ideals = newValue,
                        bonds = characterState.bonds,
                        flaws = characterState.flaws
                    )
                },
                modifier = Modifier.height(120.dp),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Next
                ),
            )

            PersonalityField(
                label = stringResource(R.string.personality_bonds_label),
                value = characterState.bonds,
                onValueChange = { newValue ->
                    sharedViewModel.updatePersonality(
                        personalTraits = characterState.personalTraits,
                        ideals = characterState.ideals,
                        bonds = newValue,
                        flaws = characterState.flaws
                    )
                },
                modifier = Modifier.height(120.dp),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Next
                ),
            )

            PersonalityField(
                label = stringResource(R.string.personality_flaws_label),
                value = characterState.flaws,
                onValueChange = { newValue ->
                    sharedViewModel.updatePersonality(
                        personalTraits = characterState.personalTraits,
                        ideals = characterState.ideals,
                        bonds = characterState.bonds,
                        flaws = newValue
                    )
                },
                modifier = Modifier.height(120.dp),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                ),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PersonalityField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: androidx.compose.foundation.text.KeyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default,
    keyboardActions: androidx.compose.foundation.text.KeyboardActions = androidx.compose.foundation.text.KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = false,
        maxLines = 5,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun PersonalityPrev() {
    Personality(
        onBack = {},
        onNext = {},
        sharedViewModel = TODO(),
    )
}