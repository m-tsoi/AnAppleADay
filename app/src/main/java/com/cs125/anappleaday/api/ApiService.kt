package com.cs125.anappleaday.api

import android.health.connect.datatypes.ExerciseSessionRecord
import android.health.connect.datatypes.NutritionRecord
import com.cs125.anappleaday.data.record.models.ProposedExercise
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("caloriesburnedactivities")
    fun getCaloriesBurnedExercises(
        @Query("activity") name: String,
        @Query("weight") type: String,
        @Query("duration") muscle: String
    ): Call<ExerciseSessionRecord>

    // Note: Optional use if required for generating recommendations
    @GET("exercises")
    fun getRecommendedExercises(
        @Query("name") name: String,
        @Query("type") type: String,
        @Query("muscle") muscle: String,
        @Query("difficulty") difficulty: String,
        @Query("offset") offset: Int
    ) : Call<ProposedExercise>

    @GET("nutrition")
    fun getNutrition(
        @Query("query") query: String   // input prompt
    ): Call<NutritionRecord>
}