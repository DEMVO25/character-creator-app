package com.example.character_creator_app.character_info.details

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel
import com.example.character_creator_app.character_info.dice_roll.DiceRoller
import com.example.character_creator_app.character_info.feature_traits.FeaturesTabContent
import com.example.character_creator_app.character_info.inventory.ItemsTabContent
import com.example.character_creator_app.character_info.main_info.InfoTabContent
import com.example.character_creator_app.character_info.personality.PersonalityTabContent
import com.example.character_creator_app.character_info.skills.SkillsTabContent
import com.example.character_creator_app.character_info.spells_sheet.SpellsTabContent
import com.example.character_creator_app.character_info.stats.StatsTabContent
import com.example.character_creator_app.character_info.weapons.WeaponsTabContent
import data.local.entity.CharacterEntity
import kotlinx.coroutines.launch


@Composable
fun DetailsScreenRoute(
    characterId: Int,
    onBack: () -> Unit,
    navigateToMainInfoUpdate: () -> Unit,
    detailsViewModel: DetailsViewModel = hiltViewModel(),
    sharedViewModel: SharedCharacterViewModel = hiltViewModel()
) {
    LaunchedEffect(characterId) {
        sharedViewModel.loadCharacter(characterId)
    }

    val character by detailsViewModel.characterState.collectAsStateWithLifecycle()

    character?.let { data ->
        DetailsScreen(
            character = data,
            onBack = onBack,
            navigateToMainInfoUpdate = navigateToMainInfoUpdate,
            viewModel = detailsViewModel,
            sharedViewModel = sharedViewModel
        )
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

sealed class CharacterScreen(
    val id: String,
    @StringRes val titleRes: Int,
    val icon: ImageVector? = null
) {
    object Info : CharacterScreen("info", R.string.main_info_label, Icons.Default.Person)
    object Stats :
        CharacterScreen("stats", R.string.stats_saving_throws_label, Icons.Default.Assignment)

    object Skills :
        CharacterScreen("skills", R.string.skills_label, Icons.Default.FormatListBulleted)

    object Personality :
        CharacterScreen("personality", R.string.personality_label, Icons.Default.Psychology)

    object Weapons :
        CharacterScreen("weapons", R.string.weapons_label, null)

    object Items : CharacterScreen("items", R.string.inventory_label, Icons.Default.Backpack)
    object Features :
        CharacterScreen("features", R.string.feature_traits_label, Icons.Default.Article)

    object Spells : CharacterScreen("spells", R.string.spellbook_label, Icons.Default.AutoStories)
    object Dice : CharacterScreen("dice", R.string.dice_roller_label, Icons.Default.Casino)

    companion object {
        val allScreens by lazy {
            listOf(Info, Stats, Skills, Personality, Weapons, Items, Features, Spells, Dice)
        }
    }
}

val CharacterScreenSaver = androidx.compose.runtime.saveable.Saver<CharacterScreen, String>(
    save = { it.id },
    restore = { id ->
        CharacterScreen.allScreens.find { it.id == id } ?: CharacterScreen.Info
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navigateToMainInfoUpdate: () -> Unit,
    character: CharacterEntity,
    onBack: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel(),
    sharedViewModel: SharedCharacterViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var currentScreen by androidx.compose.runtime.saveable.rememberSaveable(stateSaver = CharacterScreenSaver) {
        mutableStateOf(CharacterScreen.Info)
    }

    androidx.activity.compose.BackHandler(enabled = currentScreen != CharacterScreen.Info || drawerState.isOpen) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else {
            currentScreen = CharacterScreen.Info
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = stringResource(R.string.character_menu_title),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(top = 8.dp)
                ) {
                    CharacterScreen.allScreens.forEach { screen ->
                        NavigationDrawerItem(
                            label = { Text(stringResource(id = screen.titleRes)) },
                            selected = currentScreen == screen,
                            onClick = {
                                currentScreen = screen
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                if (screen.icon != null) {
                                    Icon(screen.icon, contentDescription = null)
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.swords_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.home_button_label)) },
                    selected = false,
                    onClick = onBack,
                    icon = { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) },
                    modifier = Modifier.padding(12.dp)
                )
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Column {
                                Text(character.name, style = MaterialTheme.typography.titleLarge)
                                Text(
                                    text = "${character.characterClass} • ${
                                        stringResource(
                                            R.string.level_indicator_format,
                                            character.level
                                        )
                                    }",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = stringResource(R.string.open_menu_description)
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = navigateToMainInfoUpdate) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = stringResource(R.string.edit_character_description)
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    androidx.compose.animation.Crossfade(
                        targetState = currentScreen,
                        label = "tabChange"
                    ) { screen ->
                        when (screen) {
                            CharacterScreen.Info -> InfoTabContent(character, viewModel)
                            CharacterScreen.Stats -> StatsTabContent(character)
                            CharacterScreen.Skills -> SkillsTabContent(character)
                            CharacterScreen.Personality -> PersonalityTabContent(character)
                            CharacterScreen.Weapons -> {
                                WeaponsTabContent(
                                    character = character,
                                    onWeaponsChange = { updatedList ->
                                        viewModel.updateWeaponsInfo(updatedList)
                                    }
                                )
                            }

                            CharacterScreen.Items -> ItemsTabContent(character) {
                                viewModel.updateInventoryInfo(
                                    it
                                )
                            }

                            CharacterScreen.Features -> FeaturesTabContent(character)
                            CharacterScreen.Spells -> SpellsTabContent(character, viewModel)
                            CharacterScreen.Dice -> DiceRoller()
                        }
                    }
                }
            }
        }
    )
}




