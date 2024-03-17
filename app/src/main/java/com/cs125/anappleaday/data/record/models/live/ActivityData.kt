package com.cs125.anappleaday.data.record.models.live

import android.health.connect.datatypes.ExerciseSessionRecord

data class ActivityData(
    // Similar to SleepData.kt, retrieves data from user input + recs
    val id: String,
    //val scores: MutableList<ScoreDay>, // Might not be used
    //val activities: MutableList<ExerciseSessionRecord>,
    val dailyExerciseRecords: MutableList<ExerciseSession> = mutableListOf<ExerciseSession>(),
    //val recommendedExercises: MutableList<>

    // Values
    val name: String,
    val calories_per_hour: Int,
    val duration_minutes: Double,
    val total_calories: Int,
)
