package com.cs125.anappleaday.api


import android.health.connect.datatypes.NutritionRecord
import com.cs125.anappleaday.BuildConfig
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EdamamService {
    @GET("recipes/v2")
    fun getRecipes(
        @Query("type") type: String = "public",
        @Query("q") q: String,
        @Query("calories") calories: String,
        @Query("app_id") app_id: String = "d5db0761",
        @Query("app_key") app_key: String = "9966c4e86fd00511590cd7a67c92e9ac",
    ) : Call<JsonElement>
}