package data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ClassDto(
    val name: String,
    val slug: String,
    val desc: String?,

    @SerialName("hit_dice")
    val hitDice: String,

    @SerialName("hp_at_1st_level")
    val hpAt1stLevel: String?,

    @SerialName("prof_armor")
    val profArmor: String?,

    @SerialName("prof_weapons")
    val profWeapons: String?,

    @SerialName("prof_tools")
    val profTools: String?,

    @SerialName("prof_saving_throws")
    val profSavingThrows: String?,

    @SerialName("prof_skills")
    val profSkills: String?,

    val equipment: String?,
    val table: String?
) {

    val index: String get() = slug
}


@Serializable
data class SkillsResponse(
    val count: Int,
    val results: List<SkillsEntity>
)

@Serializable
data class SkillsEntity(
    val index: String,
    val name: String,
    val url: String,

    val desc: List<String>? = null
)

@Serializable
data class LanguagesEntity(
    val index: String,
    val name: String,
    val url: String
)

@Serializable
data class LanguagesResponse(
    val count: Int,
    val results: List<LanguagesEntity>
)
