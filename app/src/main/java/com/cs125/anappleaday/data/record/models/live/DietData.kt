package com.cs125.anappleaday.data.record.models.live

import android.health.connect.datatypes.NutritionRecord

data class DietData(
    val scores: MutableList<ScoreDay> = mutableListOf(),

    val nutrition: MutableList<NutritionRecord> = mutableListOf()
)
