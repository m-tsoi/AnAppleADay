package com.cs125.anappleaday.data.record.models.live

import com.cs125.anappleaday.data.enumTypes.NutritionData

data class DietData(
    val dietScore: Int = 0,

    val dietRecords: MutableList<NutritionData> = mutableListOf() ){
    fun addNutritionData(nutritionData: NutritionData) {
        dietRecords.add(nutritionData)
    }
}
