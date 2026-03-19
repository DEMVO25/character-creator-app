package com.example.character_creator_app.level_up.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel
import com.example.character_creator_app.level_up.feature_traits.FeaturesLevelUp
import com.example.character_creator_app.level_up.main_info.MainInfoUpdateScreen
import com.example.character_creator_app.level_up.skills.SkillsLevelUp
import com.example.character_creator_app.level_up.spell_sheet.SpellsLevelUp
import com.example.character_creator_app.level_up.stats.EditStatsScreen
import kotlinx.coroutines.launch

@Composable
fun LevelUpRoute(
    onBack: () -> Unit,
    navController: NavController,

    ) {

    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry<com.example.character_creator_app.character_info.details.Details>()
    }

    val characterId = parentEntry.arguments?.getInt("characterId") ?: return

    val sharedViewModel: SharedCharacterViewModel = hiltViewModel(parentEntry)

    androidx.compose.runtime.LaunchedEffect(characterId) {
        sharedViewModel.loadCharacter(characterId)
    }

    LevelUpScreen(
        onBack = onBack,
        viewModel = sharedViewModel,
    )
}

sealed class LevelUpStep(
    @StringRes val titleRes: Int,
    val icon: ImageVector? = null,
    val customIconRes: Int? = null
) {
    data object Main : LevelUpStep(R.string.main_info_label, Icons.Default.Person)
    data object Stats : LevelUpStep(R.string.stats_saving_throws_label, Icons.Default.Assignment)
    data object Skills : LevelUpStep(R.string.skill_proficiencies_level_up_label, Icons.AutoMirrored.Filled.FormatListBulleted)
    data object Features : LevelUpStep(R.string.feature_traits_label, Icons.Default.Article)
    data object Spells : LevelUpStep(R.string.spellbook_label, Icons.Default.AutoStories)

    companion object {
        val all: List<LevelUpStep> by lazy {
            listOf(Main, Stats, Skills, Features, Spells)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelUpScreen(
    onBack: () -> Unit,
    viewModel: SharedCharacterViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val steps = LevelUpStep.all

    var currentStepIndex by androidx.compose.runtime.saveable.rememberSaveable {
        mutableStateOf(0)
    }

    val currentStep = steps.getOrElse(currentStepIndex) { LevelUpStep.Main }


    androidx.activity.compose.BackHandler(enabled = drawerState.isOpen || currentStep != LevelUpStep.Main) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else {
            currentStepIndex = 0
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            LevelUpDrawerContent(
                currentStep = currentStep,
                onStepClick = { step ->
                    currentStepIndex = steps.indexOf(step).coerceAtLeast(0)
                    scope.launch { drawerState.close() }
                },
                onFinish = {
                    viewModel.saveLevelUpChanges()
                    onBack()
                }
            )
        }
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(currentStep.titleRes)) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel")
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                androidx.compose.animation.Crossfade(
                    targetState = currentStep,
                    label = "stepAnimation"
                ) { step ->
                    when (step) {
                        LevelUpStep.Main -> MainInfoUpdateScreen(viewModel)
                        LevelUpStep.Stats -> EditStatsScreen(viewModel)
                        LevelUpStep.Skills -> SkillsLevelUp(viewModel)
                        LevelUpStep.Features -> FeaturesLevelUp(viewModel)
                        LevelUpStep.Spells -> SpellsLevelUp(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun LevelUpDrawerContent(
    currentStep: LevelUpStep,
    onStepClick: (LevelUpStep) -> Unit,
    onFinish: () -> Unit
) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.level_up_label),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                stringResource(R.string.update_level_up_label),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))

        Column(modifier = Modifier.weight(1f)) {
            LevelUpStep.all.forEach { step ->
                NavigationDrawerItem(
                    label = { Text(stringResource(step.titleRes), fontWeight = FontWeight.Medium) },
                    selected = currentStep == step,
                    onClick = { onStepClick(step) },
                    icon = {
                        if (step.icon != null) {
                            Icon(step.icon, null)
                        } else if (step.customIconRes != null) {
                            Icon(painterResource(step.customIconRes), null, Modifier.size(24.dp))
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
            Button(
                onClick = onFinish,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Check, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.complete_level_up_button_label), fontWeight = FontWeight.Bold)
            }
        }
    }
}