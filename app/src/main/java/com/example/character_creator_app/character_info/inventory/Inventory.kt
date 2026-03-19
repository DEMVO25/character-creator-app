package com.example.character_creator_app.character_info.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.equipment.EffectType
import com.example.character_creator_app.character_creation.equipment.InventoryItem
import com.example.character_creator_app.character_creation.equipment.InventoryRow
import com.example.character_creator_app.character_creation.equipment.ItemEffect
import data.local.entity.CharacterDto

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ItemsTabContent(
    character: CharacterDto,
    onInventoryChange: (List<InventoryItem>) -> Unit
) {
    val inventory = character.inventory

    var selectedStat by remember { mutableStateOf(com.example.character_creator_app.character_creation.equipment.StatType.STR) }
    var statExpanded by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var effectValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedEffectType by remember { mutableStateOf(EffectType.AC_BONUS) }
    var pendingEffects by remember { mutableStateOf(listOf<ItemEffect>()) }
    var itemToDelete by remember { mutableStateOf<InventoryItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Text(
            stringResource(R.string.new_item_label),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text(stringResource(R.string.item_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (pendingEffects.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                pendingEffects.forEach { effect ->
                    SuggestionChip(
                        onClick = { pendingEffects = pendingEffects - effect },
                        label = { Text("${effect.type.name}: ${effect.value}") },
                        icon = { Icon(Icons.Default.Clear, null, modifier = Modifier.size(16.dp)) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(0.35f)
            ) {
                OutlinedTextField(
                    value = selectedEffectType.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.inventory_type_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(),
                    textStyle = MaterialTheme.typography.bodySmall
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    EffectType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = { selectedEffectType = type; expanded = false }
                        )
                    }
                }
            }

            if (selectedEffectType == EffectType.STAT_BONUS) {
                ExposedDropdownMenuBox(
                    expanded = statExpanded,
                    onExpandedChange = { statExpanded = !statExpanded },
                    modifier = Modifier
                        .weight(0.25f)
                        .padding(start = 8.dp)
                ) {
                    OutlinedTextField(
                        value = selectedStat.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.inventory_stat_label)) },
                        modifier = Modifier.menuAnchor(),
                        textStyle = MaterialTheme.typography.bodySmall
                    )
                    ExposedDropdownMenu(
                        expanded = statExpanded,
                        onDismissRequest = { statExpanded = false }) {
                        com.example.character_creator_app.character_creation.equipment.StatType.entries.forEach { stat ->
                            DropdownMenuItem(
                                text = { Text(stat.name) },
                                onClick = { selectedStat = stat; statExpanded = false }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = effectValue,
                onValueChange = { effectValue = it },
                label = { Text(stringResource(R.string.inventory_value_label)) },
                modifier = Modifier
                    .weight(0.25f)
                    .padding(horizontal = 8.dp),
                singleLine = true
            )

            IconButton(
                onClick = {
                    if (effectValue.isNotBlank()) {
                        val formattedValue = if (selectedEffectType == EffectType.STAT_BONUS) {
                            val sign =
                                if (!effectValue.startsWith("+") && !effectValue.startsWith("-")) "+" else ""
                            "${selectedStat.name} $sign$effectValue"
                        } else {
                            effectValue
                        }
                        pendingEffects =
                            pendingEffects + ItemEffect(selectedEffectType, formattedValue)
                        effectValue = ""
                    }
                },
                modifier = Modifier.weight(0.15f)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.inventory_add_effect_desc)
                )
            }
        }

        Button(
            onClick = {
                if (itemName.isNotBlank()) {
                    val newItem = InventoryItem(name = itemName, effects = pendingEffects)
                    onInventoryChange(inventory + newItem)
                    itemName = ""
                    pendingEffects = emptyList()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            enabled = itemName.isNotBlank()
        ) {
            Text(stringResource(R.string.inventory_add_to_bag))
        }

        HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))

        Text(
            "Carried Items",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (inventory.isEmpty()) {
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.inventory_empty_state),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(inventory) { item ->
                    InventoryRow(
                        item = item,
                        onToggle = { isChecked ->
                            onInventoryChange(inventory.map {
                                if (it.id == item.id) it.copy(isEquipped = isChecked) else it
                            })
                        },
                        onDelete = { itemToDelete = item }
                    )
                }
            }
        }
    }

    itemToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text(stringResource(R.string.inventory_discard_title)) },
            text = {

                Text(stringResource(R.string.inventory_discard_confirm, item.name))
            },
            confirmButton = {
                TextButton(onClick = {
                    onInventoryChange(inventory.filter { it.id != item.id })
                    itemToDelete = null
                }) {
                    Text("Discard", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text(stringResource(R.string.inventory_keep_action))
                }
            }
        )
    }
}