package com.example.character_creator_app.character_creation.skills


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel
import com.example.character_creator_app.character_creation.shared_view_model.SkillCalculator
import data.models.SkillsEntity

@Composable
fun SkillsScreenRoute(
    onBack: () -> Unit,
    parentEntry: NavBackStackEntry,
    navigateToPersonality: () -> Unit
) {

    val sharedViewModel: SharedCharacterViewModel = hiltViewModel(parentEntry)
    val skillsViewModel: SkillsViewModel = hiltViewModel()
    val characterState by sharedViewModel.characterState.collectAsStateWithLifecycle()
    val selectedSkills by sharedViewModel.selectedSkills.collectAsStateWithLifecycle()

    SkillsScreen(
        onBack = onBack,
        skillsViewModel = skillsViewModel,
        selectedSkills = selectedSkills,
        proficiencyBonus = characterState.proficiencyBonus,
        onSkillToggle = { sharedViewModel.toggleSkill(it) },
        getModifierForSkill = { skillName ->
            SkillCalculator.getModifierForSkill(skillName, characterState)
        },
        navigateToPersonality = navigateToPersonality
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsScreen(
    onBack: () -> Unit,
    skillsViewModel: SkillsViewModel,
    selectedSkills: Set<String>,
    proficiencyBonus: Int,
    onSkillToggle: (String) -> Unit,
    getModifierForSkill: (String) -> Int,
    navigateToPersonality: () -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text(stringResource(R.string.available_skills_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                    onClick = navigateToPersonality,
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
        ) {
            Text(
                text = stringResource(R.string.proficiency_bonus_format, proficiencyBonus),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.secondary
            )



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (skillsViewModel.isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                skillsViewModel.errorMessage.value?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(skillsViewModel.skills) { skill ->
                        val skillKey = remember(skill.name) {
                            skill.name.lowercase().trim().replace(" ", "_")
                        }
                        SkillItem(
                            skill = skill,
                            proficiencyBonus = proficiencyBonus,
                            isSelected = selectedSkills.contains(skillKey),
                            abilityModifier = getModifierForSkill(skillKey),
                            onToggle = { onSkillToggle(skillKey) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SkillItem(
    skill: SkillsEntity,
    isSelected: Boolean,
    abilityModifier: Int,
    proficiencyBonus: Int,
    onToggle: (Boolean) -> Unit
) {
    val currentProficiency = if (isSelected) proficiencyBonus else 0
    val totalBonus = abilityModifier + currentProficiency
    val bonusText = if (totalBonus >= 0) "+$totalBonus" else "$totalBonus"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        onClick = { onToggle(!isSelected) }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onToggle
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = skill.name, style = MaterialTheme.typography.titleMedium)

                Text(
                    text = stringResource(R.string.skill_mod_label, abilityModifier),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = bonusText,
                style = MaterialTheme.typography.headlineSmall,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}