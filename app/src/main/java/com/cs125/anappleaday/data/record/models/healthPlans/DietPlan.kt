package com.cs125.anappleaday.data.record.models.healthPlans

data class DietPlan(
    val dayCaloriesIntake: Double,

    val dayProteinIntake: Double,

    val dayCarbIntake: Double,

    val dayFatIntake: Double,

    val supportNutrition: MutableList<String>
)
