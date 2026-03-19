package com.example.character_creator_app.level_up.main_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainInfoUpdateScreen(viewModel: SharedCharacterViewModel) {
    val character by viewModel.characterState.collectAsStateWithLifecycle()
    val maxLevel = 20

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.main_info_level_up_label), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            UpdateTextField(
                value = if (character.level == 0) "" else character.level.toString(),
                onValueChange = { newValue ->
                    if (newValue.isEmpty()) viewModel.updateLevelAndStats(0)
                    else newValue.toIntOrNull()?.let { if (it <= maxLevel) viewModel.updateLevelAndStats(it) }
                },
                label = stringResource(R.string.level_level_up_label),
                modifier = Modifier.weight(1f),
                supportingText = { Text("Max: $maxLevel") },
                isError = character.level > maxLevel,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            UpdateTextField(
                value = if (character.maxHp == 0) "" else character.maxHp.toString(),
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                        val newMax = newValue.toIntOrNull() ?: 0
                        viewModel.updateCharacterDetails { it.copy(maxHp = newMax, currentHp = newValue) }
                    }
                },
                label = stringResource(R.string.max_hp_level_up_label),
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            UpdateTextField(
                value = character.speed.toString(),
                onValueChange = { newValue ->
                    val speedValue = newValue.toIntOrNull() ?: 0
                    viewModel.updateCharacterDetails { it.copy(speed = speedValue) }
                },
                label = stringResource(R.string.speed_level_up_label),
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            UpdateTextField(
                value = if (character.initiative == 0) "" else character.initiative.toString(),
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() || it == '-' }) {
                        val init = newValue.toIntOrNull() ?: 0
                        viewModel.updateCharacterDetails { it.copy(initiative = init) }
                    }
                },
                label = stringResource(R.string.initiative_level_up_label),
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Text(stringResource(R.string.proficiencies_level_up_label), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        UpdateTextField(
            value = character.profArmor,
            onValueChange = {text-> viewModel.updateCharacterDetails { it.copy(profArmor = text) } },
            label = stringResource(R.string.armor_proficiencies_level_up_label),
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(R.string.armor_proficiencies_placeholder_level_up_label)
        )

        UpdateTextField(
            value = character.profWeapons,
            onValueChange = { text ->viewModel.updateCharacterDetails { it.copy(profWeapons = text) } },
            label = stringResource(R.string.weapon_proficiencies_level_up_label),
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(R.string.weapon_proficiencies_placeholder_level_up_label)
        )

        UpdateTextField(
            value = character.profTools,
            onValueChange = {text-> viewModel.updateCharacterDetails { it.copy(profTools = text) } },
            label = stringResource(R.string.tools_proficiencies_level_up_label),
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(R.string.tools_proficiencies_placeholder_level_up_label)
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun UpdateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    placeholder: String? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false
) {

    var textFieldValue by remember(value) {
        mutableStateOf(
            androidx.compose.ui.text.input.TextFieldValue(
                text = value,
                selection = androidx.compose.ui.text.TextRange(value.length)
            )
        )
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
            if (newValue.text != value) {
                onValueChange(newValue.text)
            }
        },
        label = { Text(label) },
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        placeholder = placeholder?.let { { Text(it) } },
        supportingText = supportingText,
        isError = isError,
        singleLine = true
    )
}