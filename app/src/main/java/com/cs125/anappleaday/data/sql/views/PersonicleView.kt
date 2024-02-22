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
    val weightRecordsId: String?,
    val dietDataId: String?,
    val activityDataId: String?,
    val relaxationDataId: String?,
    val sleepDataId: String?,
)
