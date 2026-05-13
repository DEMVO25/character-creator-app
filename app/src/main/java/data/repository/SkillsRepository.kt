package data.repository

import data.models.SkillsEntity
import data.remote.ApiService
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SkillsRepository @Inject constructor(
    @Named("DndApi") private val apiService: ApiService
) {
    private var cachedSkills: List<SkillsEntity>? = null

    suspend fun getSkills(): List<SkillsEntity> {

        cachedSkills?.let { return it }


        val response = apiService.getAllSkills()
        val results = response.results

        cachedSkills = results
        return results
    }
}