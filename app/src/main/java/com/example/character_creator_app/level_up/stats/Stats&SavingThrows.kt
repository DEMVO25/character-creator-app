package com.example.character_creator_app.level_up.stats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStatsScreen(
    viewModel: SharedCharacterViewModel,
) {
    val characterState by viewModel.characterState.collectAsStateWithLifecycle()
    val selectedSavingThrows by viewModel.selectedSavingThrows.collectAsStateWithLifecycle()
    val character = characterState ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(R.string.ability_scores_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatEditField(
                    stringResource(R.string.stat_label_str),
                    character.strength.toString(),
                    Modifier.weight(1f)
                ) {
                    updateAbility(viewModel, character, str = it)
                }
                StatEditField(
                    stringResource(R.string.stat_label_dex),
                    character.dexterity.toString(),
                    Modifier.weight(1f)
                ) {
                    updateAbility(viewModel, character, dex = it)
                }
                StatEditField(
                    stringResource(R.string.stat_label_con),
                    character.constitution.toString(),
                    Modifier.weight(1f)
                ) {
                    updateAbility(viewModel, character, con = it)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatEditField(
                    stringResource(R.string.stat_label_int),
                    character.intelligence.toString(),
                    Modifier.weight(1f)
                ) {
                    updateAbility(viewModel, character, int = it)
                }
                StatEditField(
                    stringResource(R.string.stat_label_wis),
                    character.wisdom.toString(),
                    Modifier.weight(1f)
                ) {
                    updateAbility(viewModel, character, wis = it)
                }
                StatEditField(
                    stringResource(R.string.stat_label_cha),
                    character.charisma.toString(),
                    Modifier.weight(1f)
                ) {
                    updateAbility(viewModel, character, cha = it)
                }
            }
        }



        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

        Text(
            text = stringResource(R.string.saving_throws_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                val statsList = listOf(
                    "Strength" to stringResource(R.string.stat_full_strength),
                    "Dexterity" to stringResource(R.string.stat_full_dexterity),
                    "Constitution" to stringResource(R.string.stat_full_constitution),
                    "Intelligence" to stringResource(R.string.stat_full_intelligence),
                    "Wisdom" to stringResource(R.string.stat_full_wisdom),
                    "Charisma" to stringResource(R.string.stat_full_charisma)
                )
                statsList.forEach { (id, displayName) ->
                    val isProficient = selectedSavingThrows.contains(id)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleSavingThrow(id) }
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isProficient,
                            onCheckedChange = { viewModel.toggleSavingThrow(id) }
                        )
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f),
                            fontWeight = if (isProficient) FontWeight.Bold else FontWeight.Normal
                        )
                        Text(
                            text = if (isProficient) stringResource(R.string.proficient_label) else "",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

private fun updateAbility(
    viewModel: SharedCharacterViewModel,
    c: data.local.entity.CharacterEntity,
    str: String = c.strength.toString(),
    dex: String = c.dexterity.toString(),
    con: String = c.constitution.toString(),
    int: String = c.intelligence.toString(),
    wis: String = c.wisdom.toString(),
    cha: String = c.charisma.toString()
) {
    viewModel.updateAbilityScores(str, dex, con, int, wis, cha)
}

@Composable
fun StatEditField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var textFieldValue by remember(value) {
        mutableStateOf(
            androidx.compose.ui.text.input.TextFieldValue(
                text = value,
                selection = androidx.compose.ui.text.TextRange(value.length)
            )
        )
    }

    androidx.compose.material3.OutlinedTextField(
        value = textFieldValue,
        onValueChange = { next ->
            val newText = next.text

            if (newText.isEmpty() || (newText.all { it.isDigit() } && newText.toInt() <= 30)) {
                textFieldValue = next

                if (newText != value) {
                    onValueChange(newText.ifEmpty { "0" })
                }
            }
        },
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        modifier = modifier.onFocusChanged { focusState ->

            if (focusState.isFocused) {
                textFieldValue = textFieldValue.copy(
                    selection = androidx.compose.ui.text.TextRange(0, textFieldValue.text.length)
                )
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
            imeAction = androidx.compose.ui.text.input.ImeAction.Next
        ),
        singleLine = true,
        shape = MaterialTheme.shapes.medium
    )
}