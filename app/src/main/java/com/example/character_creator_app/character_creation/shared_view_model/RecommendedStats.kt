package com.example.character_creator_app.character_creation.shared_view_model

object RecommendedStats {

    private val statsMap = mapOf(
        "barbarian" to mapOf("STR" to 17, "CON" to 16, "DEX" to 14, "WIS" to 10, "CHA" to 8, "INT" to 8),
        "fighter" to mapOf("STR" to 17, "CON" to 16, "DEX" to 14, "WIS" to 10, "CHA" to 8, "INT" to 8),
        "paladin" to mapOf("STR" to 16, "CHA" to 16, "CON" to 14, "WIS" to 10, "DEX" to 10, "INT" to 8),
        "monk" to mapOf("DEX" to 17, "WIS" to 16, "CON" to 14, "STR" to 10, "CHA" to 8, "INT" to 8),
        "ranger" to mapOf("DEX" to 17, "WIS" to 16, "CON" to 14, "STR" to 10, "CHA" to 8, "INT" to 8),
        "rogue" to mapOf("DEX" to 17, "CON" to 16, "INT" to 14, "WIS" to 10, "CHA" to 8, "STR" to 8),
        "bard" to mapOf("CHA" to 17, "DEX" to 16, "CON" to 14, "WIS" to 10, "INT" to 8, "STR" to 8),
        "sorcerer" to mapOf("CHA" to 17, "DEX" to 16, "CON" to 14, "WIS" to 10, "INT" to 8, "STR" to 8),
        "warlock" to mapOf("CHA" to 17, "DEX" to 16, "CON" to 14, "WIS" to 10, "INT" to 8, "STR" to 8),
        "wizard" to mapOf("INT" to 17, "CON" to 16, "DEX" to 14, "WIS" to 10, "CHA" to 8, "STR" to 8),
        "cleric" to mapOf("WIS" to 17, "CON" to 16, "STR" to 14, "DEX" to 10, "CHA" to 8, "INT" to 8),
        "druid" to mapOf("WIS" to 17, "CON" to 16, "DEX" to 14, "INT" to 10, "CHA" to 8, "STR" to 8)
    )

    private val defaultStats = mapOf(
        "STR" to 10, "DEX" to 10, "CON" to 10,
        "INT" to 10, "WIS" to 10, "CHA" to 10
    )

    fun getForClass(className: String): Map<String, Int> {
        return statsMap[className.lowercase()] ?: defaultStats
    }
}