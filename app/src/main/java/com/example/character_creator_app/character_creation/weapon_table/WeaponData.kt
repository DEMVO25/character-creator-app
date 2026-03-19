package com.example.character_creator_app.character_creation.weapon_table

object WeaponData {
    val simpleWeapons = listOf(
        "Club", "Dagger", "Greatclub", "Handaxe", "Javelin",
        "Light hammer", "Mace", "Quarterstaff", "Sickle", "Spear",
        "Light crossbow", "Dart", "Shortbow", "Sling"
    )

    val martialWeapons = listOf(
        "Battleaxe", "Flail", "Glaive", "Greataxe", "Greatsword",
        "Halberd", "Lance", "Longsword", "Maul", "Morningstar",
        "Pike", "Rapier", "Scimitar", "Shortsword", "Trident",
        "Warhammer", "War pick", "Whip", "Blowgun", "Hand crossbow",
        "Heavy crossbow", "Longbow", "Net"
    )

    private val weaponDamageMap = mapOf(
        "Greatsword" to "2d6", "Maul" to "2d6",
        "Greataxe" to "1d12", "Lance" to "1d12",
        "Glaive" to "1d10", "Halberd" to "1d10", "Pike" to "1d10", "Heavy crossbow" to "1d10",
        "Battleaxe" to "1d8", "Longsword" to "1d8", "Warhammer" to "1d8",
        "War pick" to "1d8", "Morningstar" to "1d8", "Flail" to "1d8",
        "Rapier" to "1d8", "Light crossbow" to "1d8", "Longbow" to "1d8",
        "Handaxe" to "1d6", "Javelin" to "1d6", "Mace" to "1d6",
        "Spear" to "1d6", "Shortsword" to "1d6", "Scimitar" to "1d6",
        "Shortbow" to "1d6", "Hand crossbow" to "1d6",
        "Club" to "1d4", "Dagger" to "1d4", "Light hammer" to "1d4",
        "Sickle" to "1d4", "Quarterstaff" to "1d4", "Dart" to "1d4",
        "Sling" to "1d4", "Whip" to "1d4"
    )

    private val weaponAbilityMap = mapOf(

        "Light crossbow" to "DEX", "Shortbow" to "DEX", "Sling" to "DEX",
        "Dart" to "DEX", "Blowgun" to "DEX", "Hand crossbow" to "DEX",
        "Heavy crossbow" to "DEX", "Longbow" to "DEX", "Net" to "DEX",
        "Dagger" to "DEX", "Rapier" to "DEX", "Scimitar" to "DEX",
        "Shortsword" to "DEX", "Whip" to "DEX"
    )

    private val weaponDamageTypeMap = mapOf(
        // Piercing
        "Dagger" to DamageType.PIERCING, "Javelin" to DamageType.PIERCING,
        "Spear" to DamageType.PIERCING, "Light crossbow" to DamageType.PIERCING,
        "Dart" to DamageType.PIERCING, "Shortbow" to DamageType.PIERCING,
        "Pike" to DamageType.PIERCING, "Rapier" to DamageType.PIERCING,
        "Shortsword" to DamageType.PIERCING, "Trident" to DamageType.PIERCING,
        "War pick" to DamageType.PIERCING, "Blowgun" to DamageType.PIERCING,
        "Hand crossbow" to DamageType.PIERCING, "Heavy crossbow" to DamageType.PIERCING,
        "Longbow" to DamageType.PIERCING, "Morningstar" to DamageType.PIERCING,
        "Lance" to DamageType.PIERCING,
        // Bludgeoning
        "Club" to DamageType.BLUDGEONING, "Greatclub" to DamageType.BLUDGEONING,
        "Light hammer" to DamageType.BLUDGEONING, "Mace" to DamageType.BLUDGEONING,
        "Quarterstaff" to DamageType.BLUDGEONING, "Sling" to DamageType.BLUDGEONING,
        "Flail" to DamageType.BLUDGEONING, "Maul" to DamageType.BLUDGEONING,
        "Warhammer" to DamageType.BLUDGEONING,
        // Slashing
        "Handaxe" to DamageType.SLASHING, "Sickle" to DamageType.SLASHING,
        "Battleaxe" to DamageType.SLASHING, "Glaive" to DamageType.SLASHING,
        "Greataxe" to DamageType.SLASHING, "Greatsword" to DamageType.SLASHING,
        "Halberd" to DamageType.SLASHING, "Longsword" to DamageType.SLASHING,
        "Scimitar" to DamageType.SLASHING, "Whip" to DamageType.SLASHING,
        // None
        "Net" to DamageType.NONE
    )

    fun getBaseDamage(weaponName: String): String = weaponDamageMap[weaponName] ?: "1d4"

    fun getWeaponAbility(weaponName: String): String = weaponAbilityMap[weaponName] ?: "STR"

    fun getWeaponDamageType(weaponName: String): String =
        weaponDamageTypeMap[weaponName]?.displayName ?: ""
}

enum class DamageType(val displayName: String) {
    PIERCING("Piercing"),
    BLUDGEONING("Bludgeoning"),
    SLASHING("Slashing"),
    NONE("None")
}