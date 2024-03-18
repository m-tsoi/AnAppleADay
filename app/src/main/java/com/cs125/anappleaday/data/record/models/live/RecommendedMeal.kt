package com.cs125.anappleaday.data.record.models.live

import com.cs125.anappleaday.data.enumTypes.MealType

data class RecommendedMeal(
    var label: String? = null,
    var ingredientsLines: MutableList<String> = mutableListOf(),
    var mealType: MealType = MealType.LUNCH,
    var calories: Double = 0.0,
    var proteinsG: Double = 0.0,
    var fatG: Double = 0.0,
    var carbG: Double = 0.0,
    var image: String? = null
)
