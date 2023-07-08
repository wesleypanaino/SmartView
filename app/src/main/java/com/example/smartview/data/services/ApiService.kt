package com.example.smartview.data.services

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun fetchData(
        @Url fullUrl: String,
        @Header("Authorization") apiKey: String?
    ): Response<ResponseBody>
}