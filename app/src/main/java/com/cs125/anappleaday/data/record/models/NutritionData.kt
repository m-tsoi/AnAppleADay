package com.cs125.anappleaday.data.record.models

import android.health.connect.datatypes.NutritionRecord

data class NutritionData(
    val id: String,

    val scores: MutableList<ScoreDay>,

    val nutrition: MutableList<NutritionRecord>
)
