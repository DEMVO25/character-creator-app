package com.example.character_creator_app.level_up.skills

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel

@Composable
fun SkillsLevelUp(
    viewModel: SharedCharacterViewModel,
) {
    val character by viewModel.characterState.collectAsStateWithLifecycle()

    val allSkills = remember {
        listOf(
            "athletics" to R.string.skill_athletics,
            "acrobatics" to R.string.skill_acrobatics,
            "sleight_of_hand" to R.string.skill_sleight_of_hand,
            "stealth" to R.string.skill_stealth,
            "arcana" to R.string.skill_arcana,
            "history" to R.string.skill_history,
            "investigation" to R.string.skill_investigation,
            "nature" to R.string.skill_nature,
            "religion" to R.string.skill_religion,
            "animal_handling" to R.string.skill_animal_handling,
            "insight" to R.string.skill_insight,
            "medicine" to R.string.skill_medicine,
            "perception" to R.string.skill_perception,
            "survival" to R.string.skill_survival,
            "deception" to R.string.skill_deception,
            "intimidation" to R.string.skill_intimidation,
            "performance" to R.string.skill_performance,
            "persuasion" to R.string.skill_persuasion
        )
    }

    val skillStats = remember {
        mapOf(
            "athletics" to "STR", "acrobatics" to "DEX", "sleight_of_hand" to "DEX",
            "stealth" to "DEX", "arcana" to "INT", "history" to "INT",
            "investigation" to "INT", "nature" to "INT", "religion" to "INT",
            "animal_handling" to "WIS", "insight" to "WIS", "medicine" to "WIS",
            "perception" to "WIS", "survival" to "WIS", "deception" to "CHA",
            "intimidation" to "CHA", "performance" to "CHA", "persuasion" to "CHA"
        )
    }

    val selectedSet = remember(character.selectedSkills) {
        character.selectedSkills.split(",").filter { it.isNotEmpty() }.toSet()
    }

    val skillsMap = remember(character.skillsValues) {
        character.skillsValues.split(",")
            .filter { it.contains(":") }
            .associate {
                val parts = it.split(":")
                parts[0] to parts[1]
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.skill_proficiencies_level_up_label),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(R.string.skill_proficiencies_description_level_up_label),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        allSkills.forEach { (skillKey, labelRes) ->
            val currentBonus = skillsMap[skillKey] ?: "+0"

            SkillSelectionRow(
                name = stringResource(labelRes),
                stat = skillStats[skillKey] ?: "",
                isSelected = selectedSet.contains(skillKey),
                currentBonus = currentBonus,
                onToggle = {
                    val newSet = if (selectedSet.contains(skillKey)) {
                        selectedSet - skillKey
                    } else {
                        selectedSet + skillKey
                    }
                    viewModel.updateSkills(newSet)
                },
                onBonusChange = { newBonus ->
                    val updatedMap = skillsMap.toMutableMap()
                    updatedMap[skillKey] = newBonus
                    val newString = allSkills.joinToString(",") { (key, _) ->
                        val value = updatedMap[key] ?: "+0"
                        "$key:$value"
                    }
                    viewModel.updateSkillsValuesManual(newString)
                }
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun SkillSelectionRow(
    name: String,
    stat: String,
    isSelected: Boolean,
    currentBonus: String,
    onToggle: () -> Unit,
    onBonusChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() }
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    text = stat,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            OutlinedTextField(
                value = currentBonus,
                onValueChange = { newValue ->
                    if (newValue.length <= 4 && newValue.all { it.isDigit() || it == '+' || it == '-' }) {
                        onBonusChange(newValue)
                    }
                },
                modifier = Modifier.width(75.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true,
                shape = CircleShape,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}