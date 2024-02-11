package com.cs125.anappleaday.data.sql.views

import androidx.room.DatabaseView

@DatabaseView("SELECT * FROM SleepPlan")
data class SleepPlanView(
    val id: String,
    val healthPlanId: String,
    val expectedDuration: Double,
    val expectedWakeUpTime: Long,
)
