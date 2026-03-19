package com.example.character_creator_app.character_creation.character_class_option

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel
import data.local.dao.CharacterDao

@Composable
fun ClassOptionScreenRoute(
    parentEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onClassSelected: (String) -> Unit
) {
    val sharedViewModel: SharedCharacterViewModel = hiltViewModel(parentEntry)

    ClassOptionScreen(
        onBack = onBack,
        onClassSelected = onClassSelected,
        sharedViewModel = sharedViewModel
    )

}

data class ClassSelectionItem(
    val techName: String,
    @androidx.annotation.StringRes val labelRes: Int
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassOptionScreen(
    onBack: () -> Unit,
    onClassSelected: (String) -> Unit,
    sharedViewModel: SharedCharacterViewModel,
) {
    val characterState by sharedViewModel.characterState.collectAsState()

    val classOptions = remember {
        listOf(
            ClassSelectionItem("Barbarian", R.string.class_barbarian),
            ClassSelectionItem("Bard", R.string.class_bard),
            ClassSelectionItem("Cleric", R.string.class_cleric),
            ClassSelectionItem("Druid", R.string.class_druid),
            ClassSelectionItem("Fighter", R.string.class_fighter),
            ClassSelectionItem("Monk", R.string.class_monk),
            ClassSelectionItem("Paladin", R.string.class_paladin),
            ClassSelectionItem("Ranger", R.string.class_ranger),
            ClassSelectionItem("Rogue", R.string.class_rogue),
            ClassSelectionItem("Sorcerer", R.string.class_sorcerer),
            ClassSelectionItem("Warlock", R.string.class_warlock),
            ClassSelectionItem("Wizard", R.string.class_wizard)
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text(stringResource(R.string.class_option_title)) },
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
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        onClassSelected(characterState.characterClass)
                    },
                    enabled = characterState.characterClass.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.next_button_label))
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.displayCutout),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.class_option_description),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.secondary
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(classOptions) { option ->
                    val isSelected = characterState.characterClass == option.techName

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = isSelected,
                                onClick = { sharedViewModel.updateClass(option.techName) }
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.RadioButton(
                            selected = isSelected,
                            onClick = { sharedViewModel.updateClass(option.techName) }
                        )

                        Text(
                            text = stringResource(option.labelRes),
                            modifier = Modifier.padding(start = 16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun ClassOptionPrev() {
    ClassOptionScreen(
        onBack = {},
        { },
        sharedViewModel = SharedCharacterViewModel(
            dao = { null } as CharacterDao
        )

    )
}