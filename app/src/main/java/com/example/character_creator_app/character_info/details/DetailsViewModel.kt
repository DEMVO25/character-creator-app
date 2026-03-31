package com.example.character_creator_app.character_info.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.character_creator_app.character_creation.equipment.EffectType
import com.example.character_creator_app.character_creation.equipment.InventoryItem
import com.example.character_creator_app.character_creation.weapon_table.WeaponRowState
import com.example.character_creator_app.character_info.skills.allCharacterSkills
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.dao.CharacterDao
import data.local.entity.CharacterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val dao: CharacterDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["characterId"])

    val characterState = dao.getCharacterById(characterId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )


    fun updateCharacterDetails(update: (CharacterEntity) -> CharacterEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            characterState.value?.let { current ->
                val updated = update(current)
                dao.upsertCharacter(updated)
            }
        }
    }


    val spellSaveDC: Int
        get() {
            val char = characterState.value ?: return 8
            val mod = getCastingMod(char.castingAbility, char)
            return 8 + char.proficiencyBonus + mod
        }


    val spellAttack: String
        get() {
            val char = characterState.value ?: return "+0"
            val mod = getCastingMod(char.castingAbility, char)
            val bonus = char.proficiencyBonus + mod
            return if (bonus >= 0) "+$bonus" else "$bonus"
        }


    private fun getCastingMod(ability: String, char: CharacterEntity): Int {
        return when (ability.uppercase()) {
            "INT" -> char.intelligenceMod
            "WIS" -> char.wisdomMod
            "CHA" -> char.charismaMod
            else -> 0
        }
    }


    fun updateSpellSlots(levelIndex: Int, newCount: Int) {
        updateCharacterDetails { current ->
            val slots = current.currentSpellSlots.split(",")
                .map { it.trim().toIntOrNull() ?: 0 }
                .toMutableList()

            if (levelIndex in slots.indices) {
                slots[levelIndex] = newCount
            }
            current.copy(currentSpellSlots = slots.joinToString(","))
        }
    }

    fun updateWeaponsInfo(newList: List<WeaponRowState>) {
        val jsonString = Gson().toJson(newList)
        updateCharacterDetails { it.copy(weaponsJson = jsonString) }
    }


    private fun calculateStatAndMod(baseScore: Int, statName: String, inventory: List<InventoryItem>): Pair<Int, Int> {
        val bonus = inventory
            .filter { it.isEquipped }
            .flatMap { it.effects }
            .filter { it.type == EffectType.STAT_BONUS }
            .filter { it.value.uppercase().contains(statName.uppercase()) }
            .sumOf { effect ->

                val regex = "(-?\\d+)".toRegex()
                val match = regex.find(effect.value)
                match?.value?.toIntOrNull() ?: 0
            }

        val finalScore = baseScore + bonus
        val mod = Math.floorDiv(finalScore - 10, 2)
        return Pair(finalScore, mod)
    }

    private fun calculateSkillsString(char: CharacterEntity, newMods: Map<String, Int>): String {

        val proficientSkills = char.selectedSkills.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        return allCharacterSkills.joinToString(",") { skill ->

            val baseMod = newMods[skill.stat] ?: 0


            val isProficient = proficientSkills.contains(skill.nameKey)


            val total = if (isProficient) baseMod + char.proficiencyBonus else baseMod
            val sign = if (total >= 0) "+" else ""


            "${skill.nameKey}:$sign$total"
        }
    }

    fun updateInventoryInfo(newList: List<InventoryItem>) {
        updateCharacterDetails { current ->

            val (strScore, strMod) = calculateStatAndMod(current.strength, "STR", newList)
            val (dexScore, dexMod) = calculateStatAndMod(current.dexterity, "DEX", newList)
            val (conScore, conMod) = calculateStatAndMod(current.constitution, "CON", newList)
            val (intScore, intMod) = calculateStatAndMod(current.intelligence, "INT", newList)
            val (wisScore, wisMod) = calculateStatAndMod(current.wisdom, "WIS", newList)
            val (chaScore, chaMod) = calculateStatAndMod(current.charisma, "CHA", newList)

            val newMods = mapOf(
                "STR" to strMod, "DEX" to dexMod, "CON" to conMod,
                "INT" to intMod, "WIS" to wisMod, "CHA" to chaMod
            )


            val updatedSkillsString = calculateSkillsString(current, newMods)

            val itemAcBonus = newList
                .filter { it.isEquipped }
                .flatMap { it.effects }
                .filter { it.type == EffectType.AC_BONUS }
                .sumOf { it.value.toIntOrNull() ?: 0 }

            current.copy(
                inventory = newList,
                updatedStrength = strScore,
                updatedDexterity = dexScore,
                updatedConstitution = conScore,
                updatedIntelligence = intScore,
                updatedWisdom = wisScore,
                updatedCharisma = chaScore,


                strengthMod = strMod,
                dexterityMod = dexMod,
                constitutionMod = conMod,
                intelligenceMod = intMod,
                wisdomMod = wisMod,
                charismaMod = chaMod,


                skillsValues = updatedSkillsString,


                armorClass = 10 + dexMod + itemAcBonus
            )
        }
    }
}