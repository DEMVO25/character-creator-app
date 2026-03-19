package com.example.character_creator_app.character_creation.character_creation_nav


import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.character_creator_app.character_creation.ability.AbilityScore
import com.example.character_creator_app.character_creation.ability.abilityScoreNavigation
import com.example.character_creator_app.character_creation.character_class_option.ClassOption
import com.example.character_creator_app.character_creation.character_class_option.classOptionNavigation
import com.example.character_creator_app.character_creation.character_languages.LanguagesOptionScreen
import com.example.character_creator_app.character_creation.character_languages.languagesOptionScreenNavigation
import com.example.character_creator_app.character_creation.class_details.ClassDetails
import com.example.character_creator_app.character_creation.class_details.classDetailsNavigation
import com.example.character_creator_app.character_creation.creation.Creation
import com.example.character_creator_app.character_creation.creation.creationNavigation
import com.example.character_creator_app.character_creation.equipment.Equipment
import com.example.character_creator_app.character_creation.equipment.equipmentNavigation
import com.example.character_creator_app.character_creation.feature_traits.FeatureTrait
import com.example.character_creator_app.character_creation.feature_traits.featureTraitNavigation
import com.example.character_creator_app.character_creation.personality.Personality
import com.example.character_creator_app.character_creation.personality.personalityNavigation
import com.example.character_creator_app.character_creation.skills.SkillsScreen
import com.example.character_creator_app.character_creation.skills.skillsScreenNavigation
import com.example.character_creator_app.character_creation.spell_sheet_creation.SpellSheetCreation
import com.example.character_creator_app.character_creation.spell_sheet_creation.spellSheetCreationNavigation
import com.example.character_creator_app.character_creation.weapon_table.WeaponTable
import com.example.character_creator_app.character_creation.weapon_table.weaponTableNavigation
import com.example.character_creator_app.nav.utils.safeNavigate
import com.example.character_creator_app.nav.utils.safePopBackStack
import kotlinx.serialization.Serializable

@Serializable
data object CharacterCreation


@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.characterCreationGraph(
    rootNavController: NavController
) {
    composable<CharacterCreation> {
        CharacterCreationNavigation(
            parentEntry = it,
            onExitCreation = { rootNavController.safePopBackStack() }
        )
    }
}

fun NavController.navigateToCharacterCreation(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(CharacterCreation, builder)
}


@Composable
fun CharacterCreationNavigation(
    onExitCreation: () -> Unit,
    parentEntry: NavBackStackEntry,
    innerNavController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = innerNavController, startDestination = Creation
    ) {
        creationNavigation(
            parentEntry = parentEntry,
            onBack = {
                if (!innerNavController.safePopBackStack()) {
                    onExitCreation()
                }
            },
            onNavigateToClassOption = { innerNavController.safeNavigate(ClassOption) }
        )

        classOptionNavigation(
            parentEntry = parentEntry,
            onBack = { innerNavController.safePopBackStack() },
            onClassSelected = { className -> innerNavController.safeNavigate(ClassDetails(className)) })

        classDetailsNavigation(
            parentEntry = parentEntry,
            onBack = { innerNavController.safePopBackStack() },
            onNavigateToAbilityScore = { innerNavController.safeNavigate(AbilityScore) }
        )

        abilityScoreNavigation(
            parentEntry = parentEntry,
            onBack = { innerNavController.safePopBackStack() },
            onNavigateToSkill = { innerNavController.safeNavigate(SkillsScreen) })

        skillsScreenNavigation(
            onBack = { innerNavController.safePopBackStack() },
            parentEntry = parentEntry,
            onNavigateToPersonality = { innerNavController.safeNavigate(Personality) })


        personalityNavigation(
            onBack = { innerNavController.safePopBackStack() },
            parentEntry = parentEntry,
            onNext = { innerNavController.safeNavigate(LanguagesOptionScreen) })

        languagesOptionScreenNavigation(
            onBack = { innerNavController.safePopBackStack() },
            parentEntry = parentEntry,
            onNavigateToEquipment = { innerNavController.safeNavigate(WeaponTable) })


        weaponTableNavigation(
            onBack = { innerNavController.safePopBackStack() },
            onNext = { innerNavController.safeNavigate(Equipment) },
            parentEntry = parentEntry
        )

        equipmentNavigation(
            onBack = { innerNavController.safePopBackStack() },
            onNext = { innerNavController.safeNavigate(FeatureTrait) },
            parentEntry = parentEntry

        )

        featureTraitNavigation(
            onBack = { innerNavController.safePopBackStack() },
            onNext = { innerNavController.safeNavigate(SpellSheetCreation) },
            parentEntry = parentEntry
        )

        spellSheetCreationNavigation(
            onBack = { innerNavController.safePopBackStack() },
            onNext = {
                if (innerNavController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                    onExitCreation()
                }
            },
            parentEntry = parentEntry
        )

    }
}

