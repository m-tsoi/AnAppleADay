package com.cs125.anappleaday.data.enumTypes

data class NutritionData (
    val name: String,
    val calories: Double,
    val servingSizeG: Double,
    val fatTotalG: Double,
    val fatSaturatedG: Double,
    val proteinG: Double,
    val sodiumMg: Int,
    val potassiumMg: Int,
    val cholesterolMg: Int,
    val carbohydratesTotalG: Int,
    val fiberG: Int,
    val sugarG: Int
) {
    constructor() : this("", 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0, 0, 0)
}