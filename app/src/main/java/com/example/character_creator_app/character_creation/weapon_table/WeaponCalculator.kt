package com.example.character_creator_app.character_creation.weapon_table

import data.local.entity.CharacterEntity

object WeaponCalculator {

    fun isProficient(proficiencies: String?, weaponName: String): Boolean {
        if (proficiencies == null) return false
        val profs = proficiencies.lowercase()
        val name = weaponName.lowercase()

        return when {
            profs.contains("martial weapons") && WeaponData.martialWeapons.any {
                it.equals(weaponName, ignoreCase = true)
            } -> true

            profs.contains("simple weapons") && WeaponData.simpleWeapons.any {
                it.equals(weaponName, ignoreCase = true)
            } -> true

            profs.contains(name) -> true

            else -> false
        }
    }

    fun calculateAttackBonus(character: CharacterEntity, weapon: WeaponRowState): String {
        val abilityMod = getAbilityModifier(character, weapon.ability)
        val proficiencyBonus = if (isProficient(character.profWeapons, weapon.weaponType)) {
            character.proficiencyBonus
        } else {
            0
        }

        val totalAtk = abilityMod + proficiencyBonus
        return if (totalAtk >= 0) "+$totalAtk" else "$totalAtk"
    }

    fun getBestAbilityMod(character: CharacterEntity, abilityText: String): Int {
        val text = abilityText.uppercase()
        val mods = buildList {
            if (text.contains("STR")) add(character.strengthMod)
            if (text.contains("DEX")) add(character.dexterityMod)
            if (text.contains("CON")) add(character.constitutionMod)
            if (text.contains("INT")) add(character.intelligenceMod)
            if (text.contains("WIS")) add(character.wisdomMod)
            if (text.contains("CHA")) add(character.charismaMod)
        }

        return mods.maxOrNull() ?: 0
    }

    private fun getAbilityModifier(character: CharacterEntity, ability: String): Int {
        return when (ability.uppercase()) {
            "STR" -> character.strengthMod
            "DEX" -> character.dexterityMod
            "CON" -> character.constitutionMod
            "INT" -> character.intelligenceMod
            "WIS" -> character.wisdomMod
            "CHA" -> character.charismaMod
            else -> 0
        }
    }
}