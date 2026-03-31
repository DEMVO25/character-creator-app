package com.example.character_creator_app.character_info.main_info

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.character_creator_app.R
import com.example.character_creator_app.character_info.details.DetailsViewModel
import data.local.entity.CharacterEntity

@Composable
fun InfoTabContent(
    character: CharacterEntity,
    viewModel: DetailsViewModel,
) {

    var hpChangeAmount by remember { mutableStateOf("") }

    var hpTextFieldValue by remember(character.currentHp) {
        mutableStateOf(
            androidx.compose.ui.text.input.TextFieldValue(
                text = character.currentHp,
                selection = androidx.compose.ui.text.TextRange(character.currentHp.length)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoStatCard(
                stringResource(R.string.stat_ac),
                character.armorClass.toString(),
                Modifier.weight(1f),
                Icons.Default.Shield
            )
            InfoStatCard(
                stringResource(R.string.stat_init),
                if (character.initiative >= 0) "+${character.initiative}" else "${character.initiative}",
                Modifier.weight(1f),
                Icons.Default.FlashOn
            )
            InfoStatCard(
                stringResource(R.string.speed_level_up_label),
                stringResource(R.string.stat_speed_format, character.speed),
                Modifier.weight(1f),
                Icons.Default.DirectionsRun
            )
        }


        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                val currentHpInt = character.currentHp.toIntOrNull() ?: 0
                val maxHpInt = character.maxHp

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.hit_points_label),
                        style = MaterialTheme.typography.titleMedium
                    )


                    OutlinedTextField(
                        value = hpTextFieldValue,
                        onValueChange = { newValue ->

                            if (newValue.text.all { it.isDigit() } || newValue.text.isEmpty()) {

                                hpTextFieldValue = newValue


                                if (newValue.text != character.currentHp) {
                                    viewModel.updateCharacterDetails { it.copy(currentHp = newValue.text) }
                                }
                            }
                        },
                        modifier = Modifier.width(130.dp),
                        textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = androidx.compose.ui.text.input.ImeAction.Done
                        ),
                        singleLine = true,
                        suffix = { Text("/ $maxHpInt") }
                    )
                }

                Spacer(Modifier.height(12.dp))


                val hpProgress =
                    if (maxHpInt > 0) (currentHpInt.toFloat() / maxHpInt.toFloat()) else 0f
                LinearProgressIndicator(
                    progress = { hpProgress.coerceIn(0f, 1f) },
                    drawStopIndicator = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(CircleShape),
                    color = if (hpProgress < 0.3f) Color.Red else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(Modifier.height(16.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val amount = hpChangeAmount.toIntOrNull() ?: 0
                            val newVal = (currentHpInt - amount).coerceAtLeast(0)
                            viewModel.updateCharacterDetails { it.copy(currentHp = newVal.toString()) }
                            hpChangeAmount = ""
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(stringResource(R.string.weapons_table_damage), color = Color.White)
                    }

                    OutlinedTextField(
                        value = hpChangeAmount,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() }) hpChangeAmount = it
                        },
                        placeholder = {
                            Text(
                                "0",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        modifier = Modifier.width(80.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            val amount = hpChangeAmount.toIntOrNull() ?: 0
                            val newVal = (currentHpInt + amount).coerceAtMost(maxHpInt)
                            viewModel.updateCharacterDetails { it.copy(currentHp = newVal.toString()) }
                            hpChangeAmount = ""
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(stringResource(R.string.hp_bar_heal_button_label), color = Color.White)
                    }
                }
            }
        }


        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    stringResource(R.string.death_saves_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    DeathSaveToggleGroup(
                        label = stringResource(R.string.death_saves_success),
                        count = character.deathSavesSuccess,
                        activeColor = Color(0xFF4CAF50),
                        onCountChange = {
                            viewModel.updateCharacterDetails { d ->
                                d.copy(
                                    deathSavesSuccess = it
                                )
                            }
                        }
                    )
                    DeathSaveToggleGroup(
                        label = stringResource(R.string.death_saves_failure),
                        count = character.deathSavesFailure,
                        activeColor = Color.Red,
                        onCountChange = {
                            viewModel.updateCharacterDetails { d ->
                                d.copy(
                                    deathSavesFailure = it
                                )
                            }
                        }
                    )
                }
                TextButton(onClick = {
                    viewModel.updateCharacterDetails {
                        it.copy(
                            deathSavesSuccess = 0,
                            deathSavesFailure = 0
                        )
                    }
                }) {
                    Text(stringResource(R.string.death_saves_reset))
                }
            }
        }


        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    stringResource(R.string.background_info_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                InfoRow(stringResource(R.string.label_alignment), character.alignment)
                InfoRow(stringResource(R.string.label_background), character.background)
                InfoRow(stringResource(R.string.label_race), character.race)
                InfoRow(stringResource(R.string.level),
                    "${character.characterClass} (${stringResource(R.string.home_level_format, character.level)})")
            }
        }


        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    stringResource(R.string.proficiencies_languages_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(Modifier.padding(vertical = 8.dp))

                ProficiencyItem(stringResource(R.string.label_armor), character.profArmor)
                ProficiencyItem(stringResource(R.string.label_weapons), character.profWeapons)
                if (character.profTools.isNotEmpty()) {
                    ProficiencyItem(stringResource(R.string.label_tools), character.profTools)
                }
                ProficiencyItem(stringResource(R.string.label_languages), character.selectedLanguages.ifEmpty { "Common" })
            }
        }
    }
}

@Composable
fun ProficiencyItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(value.ifEmpty { "None" }, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun InfoStatCard(label: String, value: String, modifier: Modifier = Modifier, icon: ImageVector) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge)
            Text(text = label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun DeathSaveToggleGroup(
    label: String,
    count: Int,
    activeColor: Color,
    onCountChange: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Row {
            for (i in 1..3) {
                val isActive = i <= count
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (isActive) activeColor else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onCountChange(if (isActive && i == count) i - 1 else i) }
                        .border(
                            1.dp,
                            if (isActive) Color.Transparent else MaterialTheme.colorScheme.outline,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {}
            }
        }
    }
}