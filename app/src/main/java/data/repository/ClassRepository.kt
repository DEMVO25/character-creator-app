package data.repository

import data.models.ClassDto
import data.remote.ApiService
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ClassRepository @Inject constructor(
    @Named("Open5e") private val apiService: ApiService
) {

    private val classCache = mutableMapOf<String, ClassDto>()

    suspend fun getClassDetails(classSlug: String): ClassDto? {

        classCache[classSlug]?.let { return it }

        val response = apiService.getClassDetails(classSlug)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                classCache[classSlug] = body
                return body
            }
        }
        return null
    }
}