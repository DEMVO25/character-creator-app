package data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.character_creator_app.character_creation.equipment.InventoryItem
import com.example.character_creator_app.character_creation.weapon_table.WeaponRowState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val characterClass: String,
    val race: String,
    val alignment: String,
    val background: String,

    val strength: Int,
    val strengthMod: Int,
    val dexterity: Int,
    val dexterityMod: Int,
    val constitution: Int,
    val constitutionMod: Int,
    val intelligence: Int,
    val intelligenceMod: Int,
    val wisdom: Int,
    val wisdomMod: Int,
    val charisma: Int,
    val charismaMod: Int,


    val updatedStrength: Int,
    val updatedDexterity: Int,
    val updatedConstitution: Int,
    val updatedIntelligence: Int,
    val updatedWisdom: Int,
    val updatedCharisma: Int,

    val skillsValues: String = "",
    val selectedSkills: String = "",

    val maxHp: Int,
    val hitDice: String = "",
    val profArmor: String = "",
    val profWeapons: String = "",
    val profSavingThrows: String = "",
    val profTools: String = "",
    val armorClass: Int = 10,
    val initiative: Int = 0,
    val speed: Int = 30,
    val currentHp: String = "",
    val deathSavesSuccess: Int = 0,
    val deathSavesFailure: Int = 0,

    val weaponsJson: String = "",

    val inventory: List<InventoryItem> = emptyList(),
    val personalTraits: String = "",
    val ideals: String = "",
    val bonds: String = "",
    val flaws: String = "",
    val selectedLanguages: String = "",

    val equipment: String = "",
    val spells: String = "",
    val spellSlots: String = "",
    val level: Int = 1,

    val proficiencyBonus: Int = 2,

    val featureTrait: String = "",
    val cantrips : String = "",
    val lvl1spells : String = "",
    val lvl2spells : String = "",
    val lvl3spells : String = "",
    val lvl4spells : String = "",
    val lvl5spells : String = "",
    val lvl6spells : String = "",
    val lvl7spells : String = "",
    val lvl8spells : String = "",
    val lvl9spells : String = "",
    val spellDcBonus: Int = 0,
    val spellAttackBonus: Int = 0,
    val maxSpellSlots: String = "0,0,0,0,0,0,0,0,0",
    val currentSpellSlots: String = "0,0,0,0,0,0,0,0,0",
    val castingAbility: String = "INT"
) {
    val weaponRows: List<WeaponRowState>
        get() = try {
            val listType = object : TypeToken<List<WeaponRowState>>() {}.type
            Gson().fromJson(weaponsJson, listType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
}