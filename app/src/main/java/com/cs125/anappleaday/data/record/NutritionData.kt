package com.cs125.anappleaday.data.record

import android.health.connect.datatypes.NutritionRecord

data class NutritionData(
    val id: String,

    val scoreData: ScoreData,

    val nutrition: List<NutritionRecord>
)
