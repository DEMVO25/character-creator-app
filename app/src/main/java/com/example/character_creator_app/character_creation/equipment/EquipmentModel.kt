package com.example.character_creator_app.character_creation.equipment

enum class EffectType {
    AC_BONUS, STAT_BONUS, RESISTANCE, VULNERABILITY, SPELL, CUSTOM
}

data class ItemEffect(
    val type: EffectType,
    val value: String,
    val label: String = ""
)

data class InventoryItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val isEquipped: Boolean = false,
    val effects: List<ItemEffect> = emptyList()
)