package com.example.character_creator_app.character_creation.shared_view_model

import data.local.entity.CharacterDto

object SkillCalculator {

    private val skillAbilityMap = mapOf(
        "athletics" to "STR",
        "acrobatics" to "DEX",
        "sleight_of_hand" to "DEX",
        "stealth" to "DEX",
        "arcana" to "INT",
        "history" to "INT",
        "investigation" to "INT",
        "nature" to "INT",
        "religion" to "INT",
        "animal_handling" to "WIS",
        "insight" to "WIS",
        "medicine" to "WIS",
        "perception" to "WIS",
        "survival" to "WIS",
        "deception" to "CHA",
        "intimidation" to "CHA",
        "performance" to "CHA",
        "persuasion" to "CHA"
    )


    val allSkillsKeys = skillAbilityMap.keys.toList()

    fun getAbilityForSkill(skillName: String): String {
        return skillAbilityMap[skillName.lowercase().replace(" ", "_")] ?: ""
    }

    fun getModifierForSkill(skillName: String, character: CharacterDto): Int {
        val ability = getAbilityForSkill(skillName)
        return when (ability) {
            "STR" -> character.strengthMod
            "DEX" -> character.dexterityMod
            "INT" -> character.intelligenceMod
            "WIS" -> character.wisdomMod
            "CHA" -> character.charismaMod
            else -> 0
        }
    }

    fun calculateSkillBonus(skillName: String, character: CharacterDto, isProficient: Boolean): Int {
        val baseMod = getModifierForSkill(skillName, character)
        return if (isProficient) baseMod + character.proficiencyBonus else baseMod
    }

    fun calculateSkillsString(character: CharacterDto, selectedSkills: Set<String>): String {
        val normalizedSelected = selectedSkills.map { it.lowercase().trim() }.toSet()
        return allSkillsKeys.joinToString(",") { key ->
            val isProficient = normalizedSelected.contains(key)
            val total = calculateSkillBonus(key, character, isProficient)
            "$key:${if (total >= 0) "+" else ""}$total"
        }
    }

    fun parseSkillsString(skillsString: String): Map<String, Int> {
        return skillsString.split(",")
            .filter { it.contains(":") }
            .associate {
                val parts = it.split(":")
                val skillName = parts[0].lowercase().trim()
                val value = parts[1].replace("+", "").toIntOrNull() ?: 0
                skillName to value
            }
    }
}