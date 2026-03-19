package com.example.character_creator_app.character_info.weapons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.weapon_table.WeaponData
import com.example.character_creator_app.character_creation.weapon_table.WeaponRowItem
import com.example.character_creator_app.character_creation.weapon_table.WeaponRowState
import data.local.entity.CharacterDto

@Composable
fun WeaponsTabContent(
    character: CharacterDto,
    onWeaponsChange: (List<WeaponRowState>) -> Unit,
    modifier: Modifier = Modifier
) {
    val weaponRows = character.weaponRows
    val customWeaponLabel = stringResource(R.string.weapons_custom_weapon)
    val weaponTypes = remember(customWeaponLabel) {
        listOf(customWeaponLabel) + WeaponData.simpleWeapons + WeaponData.martialWeapons
    }

    var weaponToDelete by remember { mutableStateOf<WeaponRowState?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        WeaponsHeader(
            onAddWeapon = { onWeaponsChange(weaponRows + WeaponRowState()) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        WeaponsTableHeader()

        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        if (weaponRows.isEmpty()) {
            EmptyWeaponsState(modifier = Modifier.weight(1f))
        } else {
            WeaponsList(
                weapons = weaponRows,
                character = character,
                weaponTypes = weaponTypes,
                onWeaponUpdate = { updated ->
                    onWeaponsChange(weaponRows.map {
                        if (it.id == updated.id) updated else it
                    })
                },
                onWeaponDelete = { weaponToDelete = it },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    weaponToDelete?.let { weapon ->
        DeleteWeaponDialog(
            weaponName = weapon.name.ifEmpty {
                stringResource(R.string.weapons_unnamed_weapon)
            },
            onConfirm = {
                onWeaponsChange(weaponRows.filter { it.id != weapon.id })
                weaponToDelete = null
            },
            onDismiss = { weaponToDelete = null }
        )
    }
}

@Composable
private fun WeaponsHeader(
    onAddWeapon: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.weapons_arsenal_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = onAddWeapon,
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.weapons_add_button)
            )
            Spacer(Modifier.width(4.dp))
            Text(stringResource(R.string.weapons_add_button))
        }
    }
}

@Composable
private fun WeaponsTableHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderCell(
            text = stringResource(R.string.weapons_table_name),
            modifier = Modifier.weight(2f)
        )
        HeaderCell(
            text = stringResource(R.string.weapons_table_atk),
            modifier = Modifier.weight(0.7f),
            textAlign = TextAlign.Center
        )
        HeaderCell(
            text = stringResource(R.string.weapons_table_damage),
            modifier = Modifier.weight(1.3f),
            textAlign = TextAlign.Center
        )
        HeaderCell(
            text = stringResource(R.string.weapons_table_stat),
            modifier = Modifier.weight(0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.width(40.dp))
    }
}

@Composable
private fun HeaderCell(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium,
        textAlign = textAlign
    )
}

@Composable
private fun EmptyWeaponsState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.weapons_empty_state),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun WeaponsList(
    weapons: List<WeaponRowState>,
    character: CharacterDto,
    weaponTypes: List<String>,
    onWeaponUpdate: (WeaponRowState) -> Unit,
    onWeaponDelete: (WeaponRowState) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = weapons,
            key = { it.id }
        ) { weapon ->
            WeaponRowItem(
                characterState = character,
                weapon = weapon,
                weaponTypes = weaponTypes,
                customWeaponLabel = weaponTypes.first(),
                onUpdate = onWeaponUpdate,
                onDelete = { onWeaponDelete(weapon) }
            )
        }
    }
}

@Composable
private fun DeleteWeaponDialog(
    weaponName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.weapons_discard_title)) },
        text = {
            Text(stringResource(R.string.weapons_discard_confirm, weaponName))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.weapons_remove_action),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.weapons_cancel_action))
            }
        }
    )
}