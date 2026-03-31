package com.example.character_creator_app.character_info.skills

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.character_creator_app.R
import com.example.character_creator_app.character_info.stats.getModForStat
import data.local.entity.CharacterEntity


data class Skill(val nameKey: String, val stat: String, @StringRes val labelRes: Int)

val allCharacterSkills = listOf(
    Skill("athletics", "STR", R.string.skill_athletics),
    Skill("acrobatics", "DEX", R.string.skill_acrobatics),
    Skill("sleight_of_hand", "DEX", R.string.skill_sleight_of_hand),
    Skill("stealth", "DEX", R.string.skill_stealth),
    Skill("arcana", "INT", R.string.skill_arcana),
    Skill("history", "INT", R.string.skill_history),
    Skill("investigation", "INT", R.string.skill_investigation),
    Skill("nature", "INT", R.string.skill_nature),
    Skill("religion", "INT", R.string.skill_religion),
    Skill("animal_handling", "WIS", R.string.skill_animal_handling),
    Skill("insight", "WIS", R.string.skill_insight),
    Skill("medicine", "WIS", R.string.skill_medicine),
    Skill("perception", "WIS", R.string.skill_perception),
    Skill("survival", "WIS", R.string.skill_survival),
    Skill("deception", "CHA", R.string.skill_deception),
    Skill("intimidation", "CHA", R.string.skill_intimidation),
    Skill("performance", "CHA", R.string.skill_performance),
    Skill("persuasion", "CHA", R.string.skill_persuasion)
)

@Composable
fun SkillsTabContent(character: CharacterEntity) {
    val proficientSkills = remember(character.selectedSkills) {
        character.selectedSkills.split(",").map { it.trim().lowercase() }.filter { it.isNotEmpty() }
    }

    val skillsMap = remember(character.skillsValues) {
        character.skillsValues.split(",")
            .filter { it.contains(":") }
            .associate {
                val parts = it.split(":")
                parts[0] to parts[1]
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.skills_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.proficiency_bonus_format, character.proficiencyBonus),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                allCharacterSkills.forEachIndexed { index, skill ->

                    val isProficient = proficientSkills.contains(skill.nameKey)
                    val bonusFromDb = skillsMap[skill.nameKey]

                    val displayBonus = if (bonusFromDb != null) {
                        bonusFromDb
                    } else {
                        val baseMod = getModForStat(skill.stat, character)
                        val calc = if (isProficient) baseMod + character.proficiencyBonus else baseMod
                        if (calc >= 0) "+$calc" else "$calc"
                    }

                    SkillRow(
                        skillName = stringResource(skill.labelRes),
                        statName = skill.stat,
                        isProficient = isProficient,
                        bonusString = displayBonus
                    )

                    if (index < allCharacterSkills.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SkillRow(
    skillName: String,
    statName: String,
    isProficient: Boolean,
    bonusString: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(
                    if (isProficient) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outlineVariant
                )
                .then(
                    if (!isProficient) Modifier.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    else Modifier
                )
        )

        Spacer(Modifier.width(16.dp))


        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = skillName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isProficient) FontWeight.SemiBold else FontWeight.Normal
            )
            Text(
                text = statName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                letterSpacing = 0.5.sp
            )
        }

        Box(
            modifier = Modifier
                .width(48.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = bonusString,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isProficient) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}