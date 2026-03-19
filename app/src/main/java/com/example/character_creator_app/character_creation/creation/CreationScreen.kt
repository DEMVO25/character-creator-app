package com.example.character_creator_app.character_creation.creation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel


@Composable
fun CreationScreenRoute(
    parentEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onNavigateToClassOption: () -> Unit,
) {

    val sharedViewModel: SharedCharacterViewModel = hiltViewModel(parentEntry)

    CreationScreen(
        onBack = onBack,
        onNavigateToClassOption = onNavigateToClassOption,
        sharedViewModel = sharedViewModel
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationScreen(
    sharedViewModel: SharedCharacterViewModel,
    onBack: () -> Unit,
    onNavigateToClassOption: () -> Unit,
) {
    val characterState by sharedViewModel.characterState.collectAsState()

    val alignmentMap = remember {
        mapOf(
            "Lawful good" to R.string.alignment_lawful_good,
            "Neutral good" to R.string.alignment_neutral_good,
            "Chaotic good" to R.string.alignment_chaotic_good,
            "Lawful neutral" to R.string.alignment_lawful_neutral,
            "True neutral" to R.string.alignment_true_neutral,
            "Chaotic neutral" to R.string.alignment_chaotic_neutral,
            "Lawful evil" to R.string.alignment_lawful_evil,
            "Neutral evil" to R.string.alignment_neutral_evil,
            "Chaotic evil" to R.string.alignment_chaotic_evil
        )
    }

    val allRaces = remember {
        listOf(
            "Human" to R.string.race_human,
            "Elf" to R.string.race_elf,
            "Dwarf" to R.string.race_dwarf,
            "Halfling" to R.string.race_halfling,
            "Dragonborn" to R.string.race_dragonborn,
            "Tiefling" to R.string.race_tiefling,
            "Gnome" to R.string.race_gnome,
            "Half-Orc" to R.string.race_half_orc
        )
    }

    var alignmentExpanded by remember { mutableStateOf(false) }
    var raceExpanded by remember { mutableStateOf(false) }

    val filteredRaces = allRaces.filter { it.first.contains(characterState.race, ignoreCase = true) }

    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text(stringResource(R.string.character_creation_label)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_label)
                        )
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
                    onClick = onNavigateToClassOption,
                    enabled = characterState.name.isNotBlank() && characterState.background.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.next_button_label),
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.2f))

            Text(
                text = stringResource(R.string.creation_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = characterState.name,
                onValueChange = { newName ->
                    sharedViewModel.updateBasicInfo(newName, characterState.race, characterState.alignment, characterState.background)
                },
                label = { Text(stringResource(R.string.character_name_label)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Next
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = raceExpanded,
                onExpandedChange = { raceExpanded = !raceExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    value = characterState.race,
                    onValueChange = { newRace ->
                        sharedViewModel.updateBasicInfo(characterState.name, newRace, characterState.alignment, characterState.background)
                        raceExpanded = true
                    },
                    label = { Text(stringResource(R.string.race_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = raceExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        imeAction = androidx.compose.ui.text.input.ImeAction.Next
                    ),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                    )
                )

                if (filteredRaces.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = raceExpanded,
                        onDismissRequest = { raceExpanded = false },
                    ) {
                        filteredRaces.forEach { (techName, resId) ->
                            DropdownMenuItem(
                                text = { Text(stringResource(resId)) },
                                onClick = {
                                    sharedViewModel.updateBasicInfo(characterState.name, techName, characterState.alignment, characterState.background)
                                    raceExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = alignmentExpanded,
                onExpandedChange = { alignmentExpanded = !alignmentExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                val displayText = alignmentMap[characterState.alignment]?.let { stringResource(it) }
                    ?: stringResource(R.string.alignment_true_neutral)

                OutlinedTextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = displayText,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.alignment_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = alignmentExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                )

                ExposedDropdownMenu(
                    expanded = alignmentExpanded,
                    onDismissRequest = { alignmentExpanded = false },
                ) {
                    alignmentMap.forEach { (techName, resId) ->
                        DropdownMenuItem(
                            text = { Text(stringResource(resId)) },
                            onClick = {
                                sharedViewModel.updateBasicInfo(characterState.name, characterState.race, techName, characterState.background)
                                alignmentExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = characterState.background,
                onValueChange = { newBg ->
                    sharedViewModel.updateBasicInfo(characterState.name, characterState.race, characterState.alignment, newBg)
                },
                label = { Text(stringResource(R.string.background_label)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                ),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}