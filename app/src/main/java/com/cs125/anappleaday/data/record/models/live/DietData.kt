package com.cs125.anappleaday.data.record.models.live

import android.health.connect.datatypes.NutritionRecord

data class DietData(
    val id: String,

    val scores: MutableList<ScoreDay>,

    val nutrition: MutableList<NutritionRecord>
)
