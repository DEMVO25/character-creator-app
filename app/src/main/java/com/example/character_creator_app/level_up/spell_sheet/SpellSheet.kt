package com.example.character_creator_app.level_up.spell_sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel
import com.example.character_creator_app.R

@Composable
fun SpellsLevelUp(
    viewModel: SharedCharacterViewModel
) {
    val character by viewModel.characterState.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            if (index == 0) {
                                stringResource(R.string.cantrips_level_up_label)
                            } else {

                                "$title ${stringResource(R.string.spells_level_up_label)}"
                            }
                        )
                    })
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            val currentText = when (selectedTabIndex) {
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

            SpellEditor(
                label = if (selectedTabIndex == 0) {
                    stringResource(R.string.known_cantrips_level_up)
                } else {
                    "${stringResource(R.string.level)} $selectedTabIndex ${stringResource(R.string.spells)}"
                },
                text = currentText,
                onValueChange = { newValue ->
                    viewModel.updateCharacterDetails { current ->
                        when (selectedTabIndex) {
                            0 -> current.copy(cantrips = newValue)
                            1 -> current.copy(lvl1spells = newValue)
                            2 -> current.copy(lvl2spells = newValue)
                            3 -> current.copy(lvl3spells = newValue)
                            4 -> current.copy(lvl4spells = newValue)
                            5 -> current.copy(lvl5spells = newValue)
                            6 -> current.copy(lvl6spells = newValue)
                            7 -> current.copy(lvl7spells = newValue)
                            8 -> current.copy(lvl8spells = newValue)
                            9 -> current.copy(lvl9spells = newValue)
                            else -> current
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SpellEditor(label: String, text: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )



        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxSize(),
            placeholder = {stringResource(R.string.spells_placeholder_level_up) },
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
            )
        )
    }
}