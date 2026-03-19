package com.example.character_creator_app.character_creation.weapon_table

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel
import data.local.entity.CharacterDto

@Composable
fun WeaponTableScreenRoute(
    onBack: () -> Unit,
    parentEntry: NavBackStackEntry,
    onNext: () -> Unit,
) {
    val sharedViewModel: SharedCharacterViewModel = hiltViewModel(parentEntry)
    val weaponRows by sharedViewModel.weaponRows.collectAsStateWithLifecycle()
    val characterState by sharedViewModel.characterState.collectAsStateWithLifecycle()

    WeaponTableScreen(
        onBack = onBack,
        onNext = onNext,
        characterState = characterState,
        weaponRows = weaponRows,
        onWeaponsChange = sharedViewModel::updateWeapons
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeaponTableScreen(
    characterState: CharacterDto,
    onBack: () -> Unit,
    onNext: () -> Unit,
    weaponRows: List<WeaponRowState>,
    onWeaponsChange: (List<WeaponRowState>) -> Unit
) {
    val customWeaponLabel = stringResource(R.string.weapons_custom_weapon)
    val weaponTypes = remember(customWeaponLabel) {
        listOf(customWeaponLabel) + WeaponData.simpleWeapons + WeaponData.martialWeapons
    }
    var weaponToDelete by remember { mutableStateOf<WeaponRowState?>(null) }

    Scaffold(
        topBar = {
            WeaponTableTopBar(onBack = onBack)
        },
        bottomBar = {
            WeaponTableBottomBar(
                onAddWeapon = { onWeaponsChange(weaponRows + WeaponRowState()) },
                onNext = onNext
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
        ) {
            WeaponTableHeader()

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(weaponRows, key = { it.id }) { weapon ->
                    WeaponRowItem(
                        characterState = characterState,
                        weapon = weapon,
                        weaponTypes = weaponTypes,
                        customWeaponLabel = customWeaponLabel,
                        onUpdate = { updated ->
                            onWeaponsChange(weaponRows.map {
                                if (it.id == weapon.id) updated else it
                            })
                        },
                        onDelete = { weaponToDelete = weapon }
                    )
                }
            }
        }

        weaponToDelete?.let { weapon ->
            DeleteWeaponDialog(
                weaponName = weapon.name.ifEmpty { stringResource(R.string.weapons_unnamed_weapon) },
                onConfirm = {
                    onWeaponsChange(weaponRows.filter { it.id != weapon.id })
                    weaponToDelete = null
                },
                onDismiss = { weaponToDelete = null }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeaponTableTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.weapons_arsenal_title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_label)
                )
            }
        }
    )
}

@Composable
private fun WeaponTableBottomBar(
    onAddWeapon: () -> Unit,
    onNext: () -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        OutlinedButton(
            onClick = onAddWeapon,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            shape = MaterialTheme.shapes.large
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.weapons_add_button))
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                text = stringResource(R.string.next_button_label),
            )
        }
    }
}

@Composable
private fun WeaponTableHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderText(stringResource(R.string.weapon_header_name_type), Modifier.weight(2.5f))
        HeaderText(stringResource(R.string.weapon_header_atk), Modifier.weight(0.7f), TextAlign.Center)
        HeaderText(stringResource(R.string.weapons_table_damage), Modifier.weight(1.5f))
        HeaderText(stringResource(R.string.weapon_header_ability), Modifier.weight(0.8f), TextAlign.Center)
        Spacer(Modifier.width(40.dp))
    }
}

@Composable
private fun HeaderText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        textAlign = textAlign
    )
}

@Composable
private fun DeleteWeaponDialog(
    weaponName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.weapon_remove_title)) },
        text = { Text(stringResource(R.string.weapon_remove_confirm, weaponName)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    stringResource(R.string.weapons_remove_action),
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