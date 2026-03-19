package data.remote

import data.models.ClassDto
import data.models.LanguagesResponse
import data.models.SkillsResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("skills")
    suspend fun getAllSkills(): SkillsResponse


    @GET("languages")
    suspend fun getAllLanguages(): LanguagesResponse


    @GET("classes/{className}")
    suspend fun getClassDetails(
        @Path("className") className: String
    ): Response<ClassDto>
}