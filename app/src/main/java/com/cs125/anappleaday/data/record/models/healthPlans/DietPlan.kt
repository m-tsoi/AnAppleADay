package com.cs125.anappleaday.data.record.models.healthPlans

data class DietPlan(
    var dayCaloriesIntake: Double = 0.0,

    var dayProteinIntake: Double = 0.0,

    var dayCarbIntake: Double = 0.0,

    var dayFatIntake: Double = 0.0,

    val supportNutrition: MutableList<String> = mutableListOf()
)
