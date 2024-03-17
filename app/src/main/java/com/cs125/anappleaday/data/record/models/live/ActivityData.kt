package com.cs125.anappleaday.data.record.models.live

import android.health.connect.datatypes.ExerciseSessionRecord
import java.util.Date

data class ActivityData(
    // Similar to SleepData.kt, retrieves data from user input + recs
    val id: String,
    //val scores: MutableList<ScoreDay>, // Might not be used
    //val activities: MutableList<ExerciseSessionRecord>,
    public val dailyExerciseRecords: MutableList<ExerciseSession> = mutableListOf<ExerciseSession>(),
    //val recommendedExercises: MutableList<>
    val scores: MutableList<ScoreDay> = mutableListOf(),
    val activities: MutableList<ExerciseSessionRecord> = mutableListOf(),
    val name: String,
    val calories_per_hour: Int,
    val duration_minutes: Double,
    val total_calories: Int,
)
