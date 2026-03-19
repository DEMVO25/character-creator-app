package com.example.character_creator_app.character_creation.shared_view_model

object CharacterCalculator {

    fun calculateModifier(score: Int): Int {
        return Math.floorDiv(score - 10, 2)
    }

    fun calculateStartingHp(hitDice: String, constitutionMod: Int): Int {
        val hitDie = parseHitDice(hitDice)
        return hitDie + constitutionMod
    }

    fun calculateProficiencyBonus(level: Int): Int {
        return when {
            level >= 17 -> 6
            level >= 13 -> 5
            level >= 9 -> 4
            level >= 5 -> 3
            else -> 2
        }
    }

    private fun parseHitDice(hitDice: String): Int {
        return hitDice.substringAfter('d').toIntOrNull() ?: 8
    }
}