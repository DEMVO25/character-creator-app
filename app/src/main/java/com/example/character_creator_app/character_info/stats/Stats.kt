package com.example.character_creator_app.character_info.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.character_creator_app.R
import data.local.entity.CharacterEntity

@Composable
fun StatsTabContent(character: CharacterEntity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.ability_scores_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            StatItem(
                stringResource(R.string.stat_label_str),
                character.updatedStrength,
                character.strengthMod
            )
            StatItem(
                stringResource(R.string.stat_label_dex),
                character.updatedDexterity,
                character.dexterityMod
            )
            StatItem(
                stringResource(R.string.stat_label_con),
                character.updatedConstitution,
                character.constitutionMod
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            StatItem(
                stringResource(R.string.stat_label_int),
                character.updatedIntelligence,
                character.intelligenceMod
            )
            StatItem(
                stringResource(R.string.stat_label_wis),
                character.updatedWisdom,
                character.wisdomMod
            )
            StatItem(
                stringResource(R.string.stat_label_cha),
                character.updatedCharisma,
                character.charismaMod
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.saving_throws_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))

        val savingThrowsData = listOf(
            Triple("STR", R.string.stat_label_str, R.string.stat_full_strength),
            Triple("DEX", R.string.stat_label_dex, R.string.stat_full_dexterity),
            Triple("CON", R.string.stat_label_con, R.string.stat_full_constitution),
            Triple("INT", R.string.stat_label_int, R.string.stat_full_intelligence),
            Triple("WIS", R.string.stat_label_wis, R.string.stat_full_wisdom),
            Triple("CHA", R.string.stat_label_cha, R.string.stat_full_charisma)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(Modifier.padding(8.dp)) {
                savingThrowsData.chunked(2).forEach { pair ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        pair.forEach { (key, shortRes, fullRes) ->
                            SavingThrowRow(
                                statKey = key,
                                shortName = stringResource(shortRes),
                                fullName = stringResource(fullRes),
                                character = character
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: Int, mod: Int) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = Modifier.padding(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = label, style = MaterialTheme.typography.labelMedium)
            Text(text = "$value", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = if (mod >= 0) "+$mod" else "$mod",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SavingThrowRow(
    statKey: String,
    shortName: String,
    fullName: String,
    character: CharacterEntity
) {
    val profs = character.profSavingThrows.split(",").map { it.trim() }
    val isProficient = profs.contains(fullName)

    val baseModifier = getModForStat(statKey, character)
    val finalBonus = if (isProficient) baseModifier + character.proficiencyBonus else baseModifier

    Row(
        modifier = Modifier
            .padding(8.dp)
            .width(140.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (isProficient) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
        )
        Spacer(Modifier.width(12.dp))
        Text(text = shortName, modifier = Modifier.weight(1f))
        Text(
            text = if (finalBonus >= 0) "+$finalBonus" else "$finalBonus",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (isProficient) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

fun getModForStat(stat: String, character: CharacterEntity): Int {
    return when (stat) {
        "STR" -> character.strengthMod
        "DEX" -> character.dexterityMod
        "CON" -> character.constitutionMod
        "INT" -> character.intelligenceMod
        "WIS" -> character.wisdomMod
        "CHA" -> character.charismaMod
        else -> 0
    }
}