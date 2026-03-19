package com.example.character_creator_app.character_creation.equipment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun EquipmentScreenRoute(
    parentEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    val sharedViewModel: SharedCharacterViewModel = hiltViewModel(parentEntry)
    EquipmentScreen(
        onBack = onBack,
        sharedViewModel = sharedViewModel,
        onNext = onNext,
    )
}

enum class StatType { STR, DEX, CON, INT, WIS, CHA }

@OptIn(
    ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class
)
@Composable
fun EquipmentScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    sharedViewModel: SharedCharacterViewModel,
) {
    val characterState by sharedViewModel.characterState.collectAsState()
    val inventory = characterState.inventory


    var itemName by remember { mutableStateOf("") }
    var effectValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var selectedEffectType by remember { mutableStateOf(EffectType.AC_BONUS) }
    var pendingEffects by remember { mutableStateOf(listOf<ItemEffect>()) }
    var itemToDelete by remember { mutableStateOf<InventoryItem?>(null) }

    var selectedStat by remember { mutableStateOf(StatType.STR) }
    var statExpanded by remember { mutableStateOf(false) }

    val currentAc = sharedViewModel.calculateCurrentAc()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text(stringResource(R.string.equipment_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
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
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = onNext, modifier = Modifier.fillMaxWidth()
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
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.armor_class_label),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text("$currentAc", style = MaterialTheme.typography.displayMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text(stringResource(R.string.item_name_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (pendingEffects.isNotEmpty()) {
                    androidx.compose.foundation.layout.FlowRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        pendingEffects.forEach { effect ->
                            androidx.compose.material3.SuggestionChip(
                                onClick = { pendingEffects = pendingEffects - effect },
                                label = { Text("${effect.type.name}: ${effect.value}") },
                                icon = { Icon(Icons.Default.Clear, null, Modifier.size(16.dp)) }
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
                            label = { Text("Type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor(),
                            textStyle = MaterialTheme.typography.bodySmall
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }) {
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
                                label = { Text("Stat") },
                                modifier = Modifier.menuAnchor(),
                                textStyle = MaterialTheme.typography.bodySmall
                            )
                            ExposedDropdownMenu(
                                expanded = statExpanded,
                                onDismissRequest = { statExpanded = false }) {
                                StatType.entries.forEach { stat ->
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
                                val formattedValue =
                                    if (selectedEffectType == EffectType.STAT_BONUS) {
                                        val sign =
                                            if (!effectValue.startsWith("+") && !effectValue.startsWith(
                                                    "-"
                                                )
                                            ) "+" else ""
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
                        Icon(Icons.Default.Add, contentDescription = "Add Effect")
                    }
                }

                Button(
                    onClick = {
                        if (itemName.isNotBlank() && pendingEffects.isNotEmpty()) {
                            val newItem = InventoryItem(name = itemName, effects = pendingEffects)
                            sharedViewModel.updateInventory(inventory + newItem)
                            itemName = ""
                            pendingEffects = emptyList()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    enabled = itemName.isNotBlank() && pendingEffects.isNotEmpty()
                ) {
                    Text(stringResource(R.string.add_to_inventory_button))
                }
            }

            Text(
                text = stringResource(R.string.your_items_header),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(inventory) { item ->
                    InventoryRow(
                        item = item,
                        onToggle = { isChecked ->
                            val newList =
                                inventory.map { if (it.id == item.id) it.copy(isEquipped = isChecked) else it }
                            sharedViewModel.updateInventory(newList)
                        },
                        onDelete = { itemToDelete = item }
                    )
                }
            }
        }
    }

    if (itemToDelete != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text(stringResource(R.string.delete_item_confirm_title)) },
            text = {
                Text(
                    stringResource(
                        R.string.delete_item_confirm_text,
                        itemToDelete?.name ?: ""
                    )
                )
            }, confirmButton = {
                TextButton(onClick = {
                    sharedViewModel.updateInventory(inventory.filter { it.id != itemToDelete?.id })
                    itemToDelete = null
                }) { Text(stringResource(R.string.delete_button), color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) { Text(stringResource(R.string.cancel_button)) }
            })
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun InventoryRow(
    item: InventoryItem, onToggle: (Boolean) -> Unit, onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete item",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                Switch(checked = item.isEquipped, onCheckedChange = onToggle)
            }

            androidx.compose.foundation.layout.FlowRow(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item.effects.forEach { effect ->
                    androidx.compose.material3.AssistChip(
                        onClick = { },
                        label = { Text("${effect.type.name}: ${effect.value}") }
                    )
                }
            }
        }
    }
}