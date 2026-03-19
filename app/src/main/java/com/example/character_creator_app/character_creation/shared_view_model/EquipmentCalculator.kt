package com.example.character_creator_app.character_creation.shared_view_model

import com.example.character_creator_app.character_creation.equipment.EffectType
import com.example.character_creator_app.character_creation.equipment.InventoryItem
import com.example.character_creator_app.character_creation.equipment.StatType
import data.local.entity.CharacterDto

object EquipmentCalculator {

    fun calculateArmorClass(character: CharacterDto): Int {
        val baseDexMod = character.dexterityMod
        val acBonus = getEquipmentBonus(character.inventory, EffectType.AC_BONUS)
        return 10 + baseDexMod + acBonus
    }



    private fun getBaseStat(statType: StatType, character: CharacterDto): Int {
        return when (statType) {
            StatType.STR -> character.strength
            StatType.DEX -> character.dexterity
            StatType.CON -> character.constitution
            StatType.INT -> character.intelligence
            StatType.WIS -> character.wisdom
            StatType.CHA -> character.charisma
        }
    }

    private fun getEquipmentBonus(
        inventory: List<InventoryItem>,
        effectType: EffectType
    ): Int {
        return inventory
            .filter { it.isEquipped }
            .flatMap { it.effects }
            .filter { it.type == effectType }
            .sumOf { it.value.toIntOrNull() ?: 0 }
    }

    private fun getStatBonus(
        inventory: List<InventoryItem>,
        targetStat: StatType
    ): Int {
        return inventory
            .filter { it.isEquipped }
            .flatMap { it.effects }
            .filter { it.type == EffectType.STAT_BONUS }
            .sumOf { parseStatBonus(it.value, targetStat) }
    }

    private fun parseStatBonus(effectValue: String, targetStat: StatType): Int {
        return try {
            val parts = effectValue.split(" ")
            val statName = parts[0]
            val value = parts[1].replace("+", "").toInt()
            if (statName == targetStat.name) value else 0
        } catch (e: Exception) {
            0
        }
    }


    fun calculateFinalStat(
        statType: StatType,
        character: CharacterDto,
        customInventory: List<InventoryItem>? = null
    ): Int {
        val baseValue = getBaseStat(statType, character)
        val inventory = customInventory ?: character.inventory
        val bonus = getStatBonus(inventory, statType)
        return baseValue + bonus
    }
}