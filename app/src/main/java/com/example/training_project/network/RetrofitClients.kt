package com.example.training_project.network

import com.example.training_project.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClients {
    private const val BASE_URL = "https://api.themoviedb.org/"
    private val okHttpClient = OkHttpClient.Builder().apply {

        addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_TOKEN}")
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