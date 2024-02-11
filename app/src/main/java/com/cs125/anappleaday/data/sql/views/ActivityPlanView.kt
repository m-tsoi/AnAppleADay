package com.cs125.anappleaday.data.sql.views

import androidx.room.DatabaseView

@DatabaseView("SELECT * FROM ActivityPlan")
data class ActivityPlanView(
    val id: String,
    val healthPlanId: String,
    val activityDataId: String,
)
