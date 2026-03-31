package com.example.character_creator_app.character_creation.ability

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel
import data.local.entity.CharacterEntity


@Composable
fun AbilityScoreScreenRoute(
    parentEntry: NavBackStackEntry,
    onNavigateToSkill: () -> Unit,
    onBack: () -> Unit
) {

    val sharedViewModel: SharedCharacterViewModel = hiltViewModel(parentEntry)

    AbilityScoreScreen(
        sharedViewModel = sharedViewModel,
        onNext = onNavigateToSkill,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbilityScoreScreen(
    sharedViewModel: SharedCharacterViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val characterState by sharedViewModel.characterState.collectAsState()
    val selectedClass = characterState.characterClass

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ability_scores_title)) },
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
                    onClick = {
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
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.ability_class_format, selectedClass),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Button(
                onClick = {
                    val rec = sharedViewModel.getRecommendedStats(selectedClass)
                    sharedViewModel.updateAbilityScores(
                        rec["STR"] ?: "10", rec["DEX"] ?: "10", rec["CON"] ?: "10",
                        rec["INT"] ?: "10", rec["WIS"] ?: "10", rec["CHA"] ?: "10"
                    )
                },
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Text(stringResource(R.string.ability_use_recommended))
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val statResources = listOf(
                    ("STR" to R.string.stat_label_str) to ("DEX" to R.string.stat_label_dex),
                    ("CON" to R.string.stat_label_con) to ("INT" to R.string.stat_label_int),
                    ("WIS" to R.string.stat_label_wis) to ("CHA" to R.string.stat_label_cha)
                )

                statResources.forEach { (stat1, stat2) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatBox(
                            label = stringResource(stat1.second),
                            value = getStatValueByKey(stat1.first, characterState),
                            onValueChange = { newValue ->
                                updateSingleStat(
                                    stat1.first,
                                    newValue,
                                    characterState,
                                    sharedViewModel
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        StatBox(
                            label = stringResource(stat2.second),
                            value = getStatValueByKey(stat2.first, characterState),
                            onValueChange = { newValue ->
                                updateSingleStat(
                                    stat2.first,
                                    newValue,
                                    characterState,
                                    sharedViewModel
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

        }
    }
}


private fun getStatValueByKey(key: String, character: CharacterEntity): String {
    return when (key) {
        "STR" -> character.strength.toString()
        "DEX" -> character.dexterity.toString()
        "CON" -> character.constitution.toString()
        "INT" -> character.intelligence.toString()
        "WIS" -> character.wisdom.toString()
        "CHA" -> character.charisma.toString()
        else -> "10"
    }
}


private fun updateSingleStat(
    label: String,
    newValue: String,
    current: CharacterEntity,
    vm: SharedCharacterViewModel
) {
    val s = if (label == "STR") newValue else current.strength.toString()
    val d = if (label == "DEX") newValue else current.dexterity.toString()
    val c = if (label == "CON") newValue else current.constitution.toString()
    val i = if (label == "INT") newValue else current.intelligence.toString()
    val w = if (label == "WIS") newValue else current.wisdom.toString()
    val ch = if (label == "CHA") newValue else current.charisma.toString()

    vm.updateAbilityScores(s, d, c, i, w, ch)
}

@Composable
fun StatBox(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textFieldValue by remember(value) {
        mutableStateOf(
            androidx.compose.ui.text.input.TextFieldValue(
                text = value,
                selection = androidx.compose.ui.text.TextRange(value.length)
            )
        )
    }
    val score = value.toIntOrNull() ?: 10
    val mod = Math.floorDiv(score - 10, 2)
    val modifierText = if (mod >= 0) "+$mod" else "$mod"

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newValue ->

                val filteredText = newValue.text.filter { it.isDigit() }


                textFieldValue = newValue.copy(text = filteredText)


                if (filteredText != value) {
                    onValueChange(filteredText)
                }
            },
            label = { Text(label) },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = androidx.compose.ui.text.input.ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),

            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
        )

        Text(
            text = stringResource(R.string.modifier_label, modifierText),
            style = MaterialTheme.typography.labelMedium,
            color = if (mod >= 0) MaterialTheme.colorScheme.primary else Color.Red,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

