package com.example.training_project.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClients {
    private const val BASE_URL = "https://api.themoviedb.org/"

    private const val TMDB_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYWEzMTU3NzQ4MDdhZDI2MWY2MDM0ZTQ5YzI1Y2E4NyIsIm5iZiI6MTc3NjkxNDM5Mi40MDEsInN1YiI6IjY5ZTk4ZmQ4NTY5MmE5ZDhkMDMzNDIxNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.m6Zcvj0XW2AsZUbHPSNznJOShtNlo9K1mtr3DPlfxUA"

    private val okHttpClient = OkHttpClient.Builder().apply {

        addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $TMDB_TOKEN")
                .addHeader("accept", "application/json")
                .build()
            chain.proceed(request)
        })

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        addInterceptor(logging)
    }.build()

    val instance: TmdbApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(TmdbApi::class.java)
    }

}