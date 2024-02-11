package com.cs125.anappleaday.data.sql.views

import androidx.room.DatabaseView

@DatabaseView("SELECT * FROM HealthPlan")
data class HealthPlanView(
    val hpId: String,
    val title: String,
    val goal: String,
)
