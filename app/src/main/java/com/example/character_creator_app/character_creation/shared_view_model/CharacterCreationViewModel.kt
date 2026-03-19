package com.example.character_creator_app.character_creation.shared_view_model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.character_creator_app.character_creation.equipment.EffectType
import com.example.character_creator_app.character_creation.equipment.InventoryItem
import com.example.character_creator_app.character_creation.equipment.StatType
import com.example.character_creator_app.character_creation.weapon_table.WeaponRowState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.dao.CharacterDao
import data.local.entity.CharacterDto
import data.models.ClassDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedCharacterViewModel @Inject constructor(
    private val dao: CharacterDao,
) : ViewModel() {


    private val gson = Gson()


    private val _characterState = MutableStateFlow(createEmptyCharacter())
    val characterState = _characterState.asStateFlow()

    private val _selectedSkills = MutableStateFlow<Set<String>>(emptySet())
    val selectedSkills = _selectedSkills.asStateFlow()

    private val _selectedLanguages = MutableStateFlow<Set<String>>(emptySet())
    val selectedLanguages = _selectedLanguages.asStateFlow()

    private val _selectedSavingThrows = MutableStateFlow<Set<String>>(emptySet())
    val selectedSavingThrows = _selectedSavingThrows.asStateFlow()

    private val _weaponRows = MutableStateFlow<List<WeaponRowState>>(emptyList())
    val weaponRows = _weaponRows.asStateFlow()



    fun updateBasicInfo(name: String, race: String, alignment: String, background: String) {
        _characterState.update {
            it.copy(
                name = name,
                race = race,
                alignment = alignment,
                background = background
            )
        }
    }

    fun updateClass(selectedClass: String) {
        _characterState.update { it.copy(characterClass = selectedClass) }
    }

    fun updateClassDetails(details: ClassDto) {
        _characterState.update { current ->
            val updated = current.copy(
                characterClass = details.name,
                hitDice = details.hitDice,
                profArmor = details.profArmor ?: "N/A",
                profWeapons = details.profWeapons ?: "N/A",
                profSavingThrows = details.profSavingThrows ?: "N/A",
                profTools = details.profTools ?: "N/A"
            )

            if (updated.maxHp == 0) {
                val startingHp = CharacterCalculator.calculateStartingHp(
                    updated.hitDice,
                    updated.constitutionMod
                )
                updated.copy(maxHp = startingHp, currentHp = startingHp.toString())
            } else {
                updated
            }
        }
    }

    fun updatePersonality(
        personalTraits: String,
        ideals: String,
        bonds: String,
        flaws: String
    ) {
        _characterState.update {
            it.copy(
                personalTraits = personalTraits,
                ideals = ideals,
                bonds = bonds,
                flaws = flaws
            )
        }
    }

    fun updateFeatureTrait(featureTrait: String) {
        _characterState.update { it.copy(featureTrait = featureTrait) }
    }

    fun updateSpellSheetCreation(cantrips: String, lvl1spells: String) {
        _characterState.update {
            it.copy(cantrips = cantrips, lvl1spells = lvl1spells)
        }
    }



    fun getRecommendedStats(className: String): Map<String, String> {
        return RecommendedStats.getForClass(className)
            .mapValues { it.value.toString() }
    }

    fun updateAbilityScores(
        str: String, dex: String, con: String,
        intel: String, wis: String, cha: String
    ) {
        _characterState.update { current ->
            val s = str.toIntOrNull() ?: current.strength
            val d = dex.toIntOrNull() ?: current.dexterity
            val c = con.toIntOrNull() ?: current.constitution
            val i = intel.toIntOrNull() ?: current.intelligence
            val w = wis.toIntOrNull() ?: current.wisdom
            val ch = cha.toIntOrNull() ?: current.charisma

            val updatedBase = current.copy(
                strength = s,
                updatedStrength = s,
                dexterity = d,
                updatedDexterity = d,
                constitution = c,
                updatedConstitution = c,
                intelligence = i,
                updatedIntelligence = i,
                wisdom = w,
                updatedWisdom = w,
                charisma = ch,
                updatedCharisma = ch,
                strengthMod = CharacterCalculator.calculateModifier(s),
                dexterityMod = CharacterCalculator.calculateModifier(d),
                initiative = CharacterCalculator.calculateModifier(d),
                constitutionMod = CharacterCalculator.calculateModifier(c),
                intelligenceMod = CharacterCalculator.calculateModifier(i),
                wisdomMod = CharacterCalculator.calculateModifier(w),
                charismaMod = CharacterCalculator.calculateModifier(ch)
            )

            recalculateCharacterStats(updatedBase)
        }
    }



    fun toggleSkill(skillName: String) {

        val currentSet = _selectedSkills.value
        val newSet = if (currentSet.contains(skillName)) {
            currentSet - skillName
        } else {
            currentSet + skillName
        }


        _selectedSkills.value = newSet


        _characterState.update { char ->
            char.copy(
                selectedSkills = newSet.joinToString(","),

                skillsValues = SkillCalculator.calculateSkillsString(char, newSet)
            )
        }
    }

    fun updateSkills(newSelectedSkills: Set<String>) {
        val char = _characterState.value

        fun String.normalizeKey() = this.trim().lowercase().replace(" ", "_")

        val oldSelected = char.selectedSkills.split(",")
            .filter { it.isNotBlank() }
            .map { it.normalizeKey() }
            .toSet()

        val normalizedNewSkills = newSelectedSkills.map { it.normalizeKey() }.toSet()

        val toggledSkill = (normalizedNewSkills - oldSelected).firstOrNull()
            ?: (oldSelected - normalizedNewSkills).firstOrNull()

        val currentValuesMap = SkillCalculator.parseSkillsString(char.skillsValues).toMutableMap()

        toggledSkill?.let { skillName ->
            val isProficient = normalizedNewSkills.contains(skillName)
            val bonus = SkillCalculator.calculateSkillBonus(skillName, char, isProficient)

            currentValuesMap[skillName] = bonus
        }

        val updatedChar = char.copy(
            selectedSkills = normalizedNewSkills.joinToString(","),
            skillsValues = currentValuesMap.entries.joinToString(",") { (key, value) ->
                "$key:${if (value >= 0) "+" else ""}$value"
            }
        )

        _characterState.value = updatedChar
        saveToDatabase(updatedChar)
    }

    fun updateSkillsValuesManual(newSkillsValuesString: String) {
        _characterState.update { it.copy(skillsValues = newSkillsValuesString) }
        saveToDatabase(_characterState.value)
    }


    fun toggleLanguage(languageName: String) {
        _selectedLanguages.update { current ->
            if (current.contains(languageName)) current - languageName
            else current + languageName
        }
    }

    fun toggleSavingThrow(statFullName: String) {
        _selectedSavingThrows.update { current ->
            val newSet = if (current.contains(statFullName)) {
                current - statFullName
            } else {
                current + statFullName
            }

            _characterState.update {
                it.copy(profSavingThrows = newSet.joinToString(", "))
            }
            newSet
        }
    }


    fun updateInventory(newList: List<InventoryItem>) {
        val updatedCharacter = _characterState.updateAndGet { current ->
            val updatedStats = updateStatsWithEquipment(current, newList)
            recalculateCharacterStats(updatedStats)
        }

        saveToDatabase(updatedCharacter)
    }

    private fun updateStatsWithEquipment(
        character: CharacterDto,
        inventory: List<InventoryItem>
    ): CharacterDto {

        val fStr = EquipmentCalculator.calculateFinalStat(StatType.STR, character, inventory)
        val fDex = EquipmentCalculator.calculateFinalStat(StatType.DEX, character, inventory)
        val fCon = EquipmentCalculator.calculateFinalStat(StatType.CON, character, inventory)
        val fInt = EquipmentCalculator.calculateFinalStat(StatType.INT, character, inventory)
        val fWis = EquipmentCalculator.calculateFinalStat(StatType.WIS, character, inventory)
        val fCha = EquipmentCalculator.calculateFinalStat(StatType.CHA, character, inventory)

        return character.copy(
            inventory = inventory,
            updatedStrength = fStr,
            updatedDexterity = fDex,
            updatedConstitution = fCon,
            updatedIntelligence = fInt,
            updatedWisdom = fWis,
            updatedCharisma = fCha,
            strengthMod = CharacterCalculator.calculateModifier(fStr),
            dexterityMod = CharacterCalculator.calculateModifier(fDex),
            constitutionMod = CharacterCalculator.calculateModifier(fCon),
            intelligenceMod = CharacterCalculator.calculateModifier(fInt),
            wisdomMod = CharacterCalculator.calculateModifier(fWis),
            charismaMod = CharacterCalculator.calculateModifier(fCha)
        )
    }

    fun getFinalStatValue(statType: StatType): Int {
        return EquipmentCalculator.calculateFinalStat(statType, _characterState.value)
    }

    fun getFinalModifier(statType: StatType): Int {
        val finalScore = getFinalStatValue(statType)
        return CharacterCalculator.calculateModifier(finalScore)
    }

    fun calculateCurrentAc(): Int {
        return EquipmentCalculator.calculateArmorClass(_characterState.value)
    }



    fun updateWeapons(newList: List<WeaponRowState>) {
        _weaponRows.value = newList
        _characterState.update { it.copy(weaponsJson = gson.toJson(newList)) }
    }


    fun updateLevelAndStats(newLevel: Int) {
        _characterState.update { current ->
            val newProficiency = CharacterCalculator.calculateProficiencyBonus(newLevel)
            val oldProficiency = current.proficiencyBonus
            val diff = newProficiency - oldProficiency

            if (diff == 0) return@update current.copy(level = newLevel)

            val updatedChar = updateSkillsForProficiencyChange(current, diff, newProficiency)
            val finalChar = updatedChar.copy(level = newLevel)

            saveToDatabase(finalChar)
            finalChar
        }
    }

    private fun updateSkillsForProficiencyChange(
        character: CharacterDto,
        proficiencyDiff: Int,
        newProficiency: Int
    ): CharacterDto {
        val currentSkillsMap = SkillCalculator.parseSkillsString(character.skillsValues)
            .toMutableMap()

        val proficientSkills = character.selectedSkills.split(",")
            .filter { it.isNotBlank() }
            .toSet()

        proficientSkills.forEach { skillName ->
            currentSkillsMap[skillName]?.let { currentValue ->
                val newValue = currentValue + proficiencyDiff
                currentSkillsMap[skillName] = newValue
            }
        }

        val newSkillsString = currentSkillsMap.entries.joinToString(",") {
            val value = it.value
            "${it.key}:${if (value >= 0) "+$value" else "$value"}"
        }

        return character.copy(
            proficiencyBonus = newProficiency,
            skillsValues = newSkillsString
        )
    }


    fun loadCharacter(characterId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val character = dao.getCharacterById(characterId).firstOrNull()
            character?.let { dbCharacter ->
                _characterState.value = dbCharacter
                _weaponRows.value = parseWeaponsJson(dbCharacter.weaponsJson)
                _selectedSkills.value = parseCommaSeparated(dbCharacter.selectedSkills)
                _selectedLanguages.value = parseCommaSeparated(dbCharacter.selectedLanguages)
                _selectedSavingThrows.value = parseSavingThrows(dbCharacter.profSavingThrows)
            }
        }
    }

    fun saveCharacterToDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val characterToWrite = _characterState.updateAndGet { current ->
                current.copy(
                    selectedSkills = _selectedSkills.value.joinToString(","),
                    selectedLanguages = _selectedLanguages.value.joinToString(","),
                    weaponsJson = gson.toJson(_weaponRows.value),
                    armorClass = EquipmentCalculator.calculateArmorClass(current),
                    skillsValues = SkillCalculator.calculateSkillsString(
                        current.copy(selectedSkills = _selectedSkills.value.joinToString(",")),
                        _selectedSkills.value
                    )
                )
            }

            val newId = dao.upsertCharacter(characterToWrite)

            if (characterToWrite.id == 0) {
                _characterState.update { it.copy(id = newId.toInt()) }
            }
        }
    }

    fun saveLevelUpChanges() {
        _characterState.update { current ->
            val finalCharacter = current.copy(
                selectedLanguages = _selectedLanguages.value.joinToString(","),
                weaponsJson = gson.toJson(_weaponRows.value),
                armorClass = EquipmentCalculator.calculateArmorClass(current)
            )

            saveToDatabase(finalCharacter)
            finalCharacter
        }
    }

    fun updateCharacterDetails(update: (CharacterDto) -> CharacterDto) {
        _characterState.update { current ->
            val updated = update(current)
            val finalWithAc = updated.copy(
                armorClass = EquipmentCalculator.calculateArmorClass(updated)
            )

            saveToDatabase(finalWithAc)
            finalWithAc
        }
    }

    private fun saveToDatabase(character: CharacterDto) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = dao.upsertCharacter(character)
            if (character.id == 0) {
                _characterState.update { it.copy(id = newId.toInt()) }
            }
        }
    }


    private fun recalculateCharacterStats(character: CharacterDto): CharacterDto {
        val selectedSkills = character.selectedSkills.split(",")
            .filter { it.isNotBlank() }
            .toSet()

        return character.copy(
            armorClass = EquipmentCalculator.calculateArmorClass(character),
            skillsValues = SkillCalculator.calculateSkillsString(character, selectedSkills)
        )
    }

    private fun parseWeaponsJson(json: String?): List<WeaponRowState> {
        if (json.isNullOrEmpty()) return emptyList()

        return try {
            val type = object : TypeToken<List<WeaponRowState>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parseCommaSeparated(str: String): Set<String> {
        return str.split(",").filter { it.isNotEmpty() }.toSet()
    }

    private fun parseSavingThrows(str: String): Set<String> {
        return str.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
    }


    fun getModifierForAbility(abilityName: String): Int {
        val char = _characterState.value
        return when (abilityName.uppercase()) {
            "STR" -> char.strengthMod
            "DEX" -> char.dexterityMod
            "CON" -> char.constitutionMod
            "INT" -> char.intelligenceMod
            "WIS" -> char.wisdomMod
            "CHA" -> char.charismaMod
            else -> 0
        }
    }

    fun getModifierForSkill(skillName: String): Int {
        return SkillCalculator.getModifierForSkill(skillName, _characterState.value)
    }

    fun getFinalStatValue(statType: StatType, state: CharacterDto, currentInventory: List<InventoryItem>): Int {
        val baseValue = when (statType) {
            StatType.STR -> state.strength
            StatType.DEX -> state.dexterity
            StatType.CON -> state.constitution
            StatType.INT -> state.intelligence
            StatType.WIS -> state.wisdom
            StatType.CHA -> state.charisma
        }

        val bonus = currentInventory
            .filter { it.isEquipped }
            .flatMap { it.effects }
            .filter { it.type == EffectType.STAT_BONUS }
            .sumOf { parseStatBonus(it.value, statType) }

        return baseValue + bonus
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


    private fun createEmptyCharacter() = CharacterDto(
        name = "",
        characterClass = "",
        race = "",
        alignment = "",
        background = "",
        strength = 0,
        strengthMod = 0,
        dexterity = 0,
        dexterityMod = 0,
        constitution = 0,
        constitutionMod = 0,
        intelligence = 0,
        intelligenceMod = 0,
        wisdom = 0,
        wisdomMod = 0,
        charisma = 0,
        charismaMod = 0,
        personalTraits = "",
        ideals = "",
        bonds = "",
        flaws = "",
        proficiencyBonus = 2,
        maxHp = 0,
        inventory = emptyList(),
        featureTrait = "",
        cantrips = "",
        lvl1spells = "",
        selectedSkills = "",
        selectedLanguages = "",
        updatedStrength = 10,
        updatedDexterity = 10,
        updatedConstitution = 10,
        updatedIntelligence = 10,
        updatedWisdom = 10,
        updatedCharisma = 10
    )
}