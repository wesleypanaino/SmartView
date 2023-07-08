package com.example.smartview.di

import com.example.smartview.data.repository.ApiRepository
import com.example.smartview.data.services.ApiService
import com.example.smartview.data.services.OpenAiService
import com.example.smartview.viewmodel.ApiViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://google.com") // placeholder, will not be used
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenAiService(): OpenAiService {
        val BASE_URL = "https://api.openai.com"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenAiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiRepository(apiService: ApiService, openAiService: OpenAiService): ApiRepository {
        return ApiRepository(apiService,openAiService)
    }
}