package com.cs125.anappleaday.api

import com.cs125.anappleaday.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiMain {
    companion object {

        private fun getApiServices(url: String, key: String = ""): Retrofit {
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            val authInterceptor = Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("X-Api-Key", key)
                builder.header("Accept-Path", true.toString())
                return@Interceptor chain.proceed(builder.build())
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit
        }

        fun getNinjaServices(): NinjaService {
            return getApiServices("https://api.api-ninjas.com/v1/",
                "vXw8nrEUVqjNp1HQtqYEhw==Ll6OUX0i7EuFlI3q").create(NinjaService::class.java)
        }


        fun getEdamamServices(): EdamamService {
            return getApiServices("https://api.edamam.com/api/")
                .create(EdamamService::class.java)
        }
    }
}