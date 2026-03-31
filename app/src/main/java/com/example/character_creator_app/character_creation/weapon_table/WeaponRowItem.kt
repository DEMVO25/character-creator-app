package com.example.character_creator_app.character_creation.weapon_table

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.character_creator_app.R
import data.local.entity.CharacterEntity

@Composable
fun WeaponRowItem(
    characterState: CharacterEntity,
    weapon: WeaponRowState,
    weaponTypes: List<String>,
    customWeaponLabel: String,
    onUpdate: (WeaponRowState) -> Unit,
    onDelete: () -> Unit
) {
    var typeDropdownExpanded by remember { mutableStateOf(false) }

    val isProficient = WeaponCalculator.isProficient(
        characterState.profWeapons,
        weapon.weaponType
    )
    val attackBonus = WeaponCalculator.calculateAttackBonus(characterState, weapon)
    val damageModifier = WeaponCalculator.getBestAbilityMod(characterState, weapon.ability)
    val damageModText = if (damageModifier >= 0) "+$damageModifier" else "$damageModifier"

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(2.5f)) {
                EditableTextField(
                    value = weapon.name,
                    onValueChange = { onUpdate(weapon.copy(name = it)) },
                    placeholder = stringResource(R.string.weapon_name_placeholder)
                )

                WeaponTypeDropdown(
                    weaponType = weapon.weaponType,
                    isProficient = isProficient,
                    expanded = typeDropdownExpanded,
                    onExpandedChange = { typeDropdownExpanded = it },
                    weaponTypes = weaponTypes,
                    customWeaponLabel = customWeaponLabel,
                    onTypeSelected = { type ->
                        onUpdate(
                            weapon.copy(
                                weaponType = type,
                                name = if (type == customWeaponLabel) "" else type,
                                ability = WeaponData.getWeaponAbility(type),
                                damageDice = WeaponData.getBaseDamage(type),
                                damageType = WeaponData.getWeaponDamageType(type)
                            )
                        )
                        typeDropdownExpanded = false
                    }
                )
            }

            // Attack bonus
            Text(
                text = attackBonus,
                modifier = Modifier.weight(0.7f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            // Damage column
            Column(Modifier.weight(1.5f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    EditableTextField(
                        value = weapon.damageDice,
                        onValueChange = { onUpdate(weapon.copy(damageDice = it)) },
                        modifier = Modifier.weight(1f),
                        placeholder = stringResource(R.string.weapon_damage_placeholder),
                        filterRegex = Regex("[0-9dD+]"),
                        maxLength = 6
                    )
                    Text(
                        text = " $damageModText",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                EditableTextField(
                    value = weapon.damageType,
                    onValueChange = { onUpdate(weapon.copy(damageType = it)) },
                    placeholder = stringResource(R.string.weapon_damage_type_placeholder)
                )
            }

            // Ability column
            EditableTextField(
                value = weapon.ability,
                onValueChange = { onUpdate(weapon.copy(ability = it.uppercase())) },
                modifier = Modifier.weight(0.8f),
                placeholder = stringResource(R.string.weapon_ability_placeholder),
                filterRegex = Regex("[a-zA-Z/]"),
                maxLength = 7
            )

            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.weapons_remove_action),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Properties field
        PropertiesField(
            properties = weapon.properties,
            onPropertiesChange = { onUpdate(weapon.copy(properties = it)) }
        )

        HorizontalDivider(thickness = 0.5.dp)
    }
}

@Composable
private fun WeaponTypeDropdown(
    weaponType: String,
    isProficient: Boolean,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    weaponTypes: List<String>,
    customWeaponLabel: String,
    onTypeSelected: (String) -> Unit
) {
    Box(Modifier.padding(start = 4.dp)) {
        Text(
            text = weaponType.ifEmpty { stringResource(R.string.weapon_type_select) },
            style = MaterialTheme.typography.bodySmall,
            color = if (!isProficient && weaponType.isNotEmpty()) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            },
            modifier = Modifier.clickable { onExpandedChange(true) }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            weaponTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = { onTypeSelected(type) }
                )
            }
        }
    }
}

@Composable
private fun PropertiesField(
    properties: String,
    onPropertiesChange: (String) -> Unit
) {
    BasicTextField(
        value = properties,
        onValueChange = onPropertiesChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 4.dp),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        decorationBox = { innerTextField ->
            if (properties.isEmpty()) {
                Text(
                    stringResource(R.string.weapon_properties_placeholder),
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            innerTextField()
        }
    )
}