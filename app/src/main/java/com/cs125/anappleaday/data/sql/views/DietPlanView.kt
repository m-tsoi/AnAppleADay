package com.cs125.anappleaday.data.sql.views

import androidx.room.DatabaseView

@DatabaseView("SELECT * FROM DietPlan")
data class DietPlanView(
    val id: String,
    val healthPlanId: String,
    val nutritionDataId: String,
)
