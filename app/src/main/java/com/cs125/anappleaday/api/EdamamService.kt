package com.cs125.anappleaday.api

import android.health.connect.datatypes.NutritionRecord
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
        @Query("app_id") app_id: String = ApiMain.edamamId,
        @Query("app_key") app_key: String = ApiMain.edamamKey,
    ) : Call<JsonElement>
}