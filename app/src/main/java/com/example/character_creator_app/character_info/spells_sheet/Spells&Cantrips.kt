package com.example.character_creator_app.character_info.spells_sheet

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.character_creator_app.R
import com.example.character_creator_app.character_info.details.DetailsViewModel
import data.local.entity.CharacterDto

@Composable
fun SpellsTabContent(
    character: CharacterDto,
    viewModel: DetailsViewModel
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

    var showMaxSlotsDialog by remember { mutableStateOf(false) }
    var showDcEditDialog by remember { mutableStateOf(false) }
    var showAtkEditDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.5f
                )
            )
        ) {
            Row(
                Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CastingAbilityPicker(character, viewModel)
                }

                VerticalDivider(
                    Modifier
                        .height(32.dp)
                        .padding(horizontal = 8.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDcEditDialog = true }
                ) {
                    Text(
                        stringResource(R.string.spells_dc_label),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        "${viewModel.spellSaveDC}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                VerticalDivider(
                    Modifier
                        .height(32.dp)
                        .padding(horizontal = 8.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showAtkEditDialog = true }
                ) {
                    Text(
                        stringResource(R.string.spells_attack_label),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        viewModel.spellAttack,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        if (selectedTabIndex > 0) {
            val levelIndex = selectedTabIndex - 1
            val maxSlots = getSlotValue(character.maxSpellSlots, levelIndex)
            val currentSlots = getSlotValue(character.currentSpellSlots, levelIndex)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.spells_level_slots_title, selectedTabIndex),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = { showMaxSlotsDialog = true }) {
                        Text(
                            text = stringResource(R.string.spells_edit_max_label),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (i in 1..maxSlots) {
                        val isFilled = i <= currentSlots
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (isFilled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                .clickable {
                                    val newCurrent = if (isFilled) i - 1 else i
                                    val newVal = updateSlotString(
                                        character.currentSpellSlots,
                                        levelIndex,
                                        newCurrent
                                    )
                                    viewModel.updateCharacterDetails { it.copy(currentSpellSlots = newVal) }
                                }
                        )
                    }
                }
            }
        }

        HorizontalDivider()

        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]))
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            if (index == 0) stringResource(R.string.spells_cantrips_label)
                            else stringResource(R.string.spells_level_tab_format, title)
                        )
                    }
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                val currentSpells = when (selectedTabIndex) {
                    0 -> character.cantrips
                    1 -> character.lvl1spells
                    2 -> character.lvl2spells
                    3 -> character.lvl3spells
                    4 -> character.lvl4spells
                    5 -> character.lvl5spells
                    6 -> character.lvl6spells
                    7 -> character.lvl7spells
                    8 -> character.lvl8spells
                    9 -> character.lvl9spells
                    else -> ""
                }

                if (currentSpells.isBlank()) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.spells_no_spells),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                } else {
                    SpellLevelView(
                        label = if (selectedTabIndex == 0) stringResource(R.string.spells_known_cantrips)
                        else stringResource(R.string.spells_known_level_format, selectedTabIndex),
                        spells = currentSpells
                    )
                }
            }
        }
    }


    if (showMaxSlotsDialog) {
        val levelIndex = selectedTabIndex - 1
        NumberEditDialog(
            title = stringResource(R.string.spells_dialog_max_slots, selectedTabIndex),
            initialValue = getSlotValue(character.maxSpellSlots, levelIndex),
            onDismiss = { showMaxSlotsDialog = false },
            onSave = { newValue ->
                val updatedMax = updateSlotString(character.maxSpellSlots, levelIndex, newValue)
                val currentVal = getSlotValue(character.currentSpellSlots, levelIndex)
                val updatedCurrent = if (currentVal > newValue) updateSlotString(
                    character.currentSpellSlots,
                    levelIndex,
                    newValue
                ) else character.currentSpellSlots
                viewModel.updateCharacterDetails {
                    it.copy(
                        maxSpellSlots = updatedMax,
                        currentSpellSlots = updatedCurrent
                    )
                }
            }
        )
    }

    if (showDcEditDialog) {
        NumberEditDialog(
            title = stringResource(R.string.spells_dialog_dc_bonus),
            initialValue = character.spellDcBonus,
            onDismiss = { showDcEditDialog = false },
            onSave = { newValue -> viewModel.updateCharacterDetails { it.copy(spellDcBonus = newValue) } }
        )
    }

    if (showAtkEditDialog) {
        NumberEditDialog(
            title = stringResource(R.string.spells_dialog_atk_bonus),
            initialValue = character.spellAttackBonus,
            onDismiss = { showAtkEditDialog = false },
            onSave = { newValue -> viewModel.updateCharacterDetails { it.copy(spellAttackBonus = newValue) } }
        )
    }
}

@Composable
fun SpellLevelView(label: String, spells: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val spellList = spells.split(Regex("[,\\n]")).filter { it.isNotBlank() }

        spellList.forEach { spellName ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = spellName.trim(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}


fun updateSlotString(oldString: String, index: Int, newValue: Int): String {
    val list = oldString.split(",").map { it.trim().toIntOrNull() ?: 0 }.toMutableList()
    if (index in list.indices) {
        list[index] = newValue.coerceAtLeast(0)
    }
    return list.joinToString(",")
}

fun getSlotValue(slotString: String, index: Int): Int {
    val list = slotString.split(",").map { it.trim().toIntOrNull() ?: 0 }
    return list.getOrNull(index) ?: 0
}

@Composable
fun CastingAbilityPicker(character: CharacterDto, viewModel: DetailsViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val abilities = listOf("INT", "WIS", "CHA")

    Box {
        Column(Modifier.clickable { expanded = true }) {
            Text(
                text = stringResource(R.string.spells_ability_title),
                style = MaterialTheme.typography.labelSmall
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(character.castingAbility.ifEmpty { "INT" }, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.ArrowDropDown, null, Modifier.size(16.dp))
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            abilities.forEach { abl ->
                DropdownMenuItem(
                    text = { Text(abl) },
                    onClick = {
                        viewModel.updateCharacterDetails { it.copy(castingAbility = abl) }
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun NumberEditDialog(
    title: String,
    initialValue: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var textValue by remember { mutableStateOf(initialValue.toString()) }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            androidx.compose.material3.OutlinedTextField(
                value = textValue,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '-' }) textValue = it },
                label = { Text(stringResource(R.string.spells_bonus_field_label)) },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(textValue.toIntOrNull() ?: 0)
                onDismiss()
            }) { Text(stringResource(R.string.spells_save_button)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss)
            {
                Text(stringResource(R.string.spells_cancel_button))
            }
        }
    )
}