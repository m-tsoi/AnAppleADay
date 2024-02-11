package com.cs125.anappleaday.data.sql.views

import androidx.room.DatabaseView

@DatabaseView("SELECT * FROM Personicle")
data class PersonicleView(
    val id: String,
    val profileId: String,
    val healthScore: Double,
    val height: Double,
    val weight: Double,
    val bmi: Double,
    val rmr: Double,
    val bodyCalories: Double,
    val bodyFat: Double,
    val nutritionDataId: String?,
    val activityDataId: String?,
    val comfortDataId: String?,
    val sleepDataId: String?,
    val weightRecordsId: String?,
    val bodyFatRecordsId: String?,
    val totalCaloriesBurnedRecordsId: String?,
)
