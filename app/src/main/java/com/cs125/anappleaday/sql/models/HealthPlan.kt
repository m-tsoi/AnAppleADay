package com.cs125.anappleaday.sql.models

import androidx.room.PrimaryKey

data class HealthPlan(
    @PrimaryKey val hpid: String,

    val userId: String,

    val dietsId: String,

    val activitiesId: String,

    val sleepsId: String,
)
