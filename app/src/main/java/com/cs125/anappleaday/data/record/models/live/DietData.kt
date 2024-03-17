package com.cs125.anappleaday.data.record.models.live

import com.cs125.anappleaday.data.enumTypes.NutritionData
import java.util.Date


data class DietData(
    val scores: MutableList<ScoreDay> = mutableListOf(),
    val nutrition: MutableMap<Date, MutableList<NutritionData>> =  mutableMapOf()
)
