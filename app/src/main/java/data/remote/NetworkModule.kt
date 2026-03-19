package data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }



    @Provides
    @Singleton
    @Named("DndRetrofit")
    fun provideDndRetrofit(json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.dnd5eapi.co/api/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    @Named("Open5eRetrofit")
    fun provideOpen5eRetrofit(json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.open5e.com/v1/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }


    @Provides
    @Singleton
    @Named("DndApi")
    fun provideDndApiService(@Named("DndRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("Open5e")
    fun provideOpen5eApiService(@Named("Open5eRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
