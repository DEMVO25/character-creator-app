package com.example.character_creator_app.character_creation.weapon_table

data class WeaponRowState(
    val id: Long = java.util.UUID.randomUUID().mostSignificantBits,
    val name: String = "",
    val weaponType: String = "",
    val atkBonus: String = "",
    val damageDice: String = "",
    val damageType: String = "",
    val ability: String = "STR",
    val properties: String = ""
)
