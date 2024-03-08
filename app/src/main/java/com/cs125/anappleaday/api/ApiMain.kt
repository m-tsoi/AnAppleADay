package com.cs125.anappleaday.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiMain {
    companion object {
        fun getAPIServices(): ApiService {

            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            
            val authInterceptor = Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("X-Api-Key", "vXw8nrEUVqjNp1HQtqYEhw==Ll6OUX0i7EuFlI3q")
                builder.header("Accept-Path", true.toString())
                return@Interceptor chain.proceed(builder.build())
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.api-ninjas.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}