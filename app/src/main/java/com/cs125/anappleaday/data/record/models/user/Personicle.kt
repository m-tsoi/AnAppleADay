package com.cs125.anappleaday.data.record.models.user

data class Personicle(
    var healthScore: Double = 0.0,

    // Body Mass Index
    var bmi: Double = 0.0,

    // Rest Metabolic Rate
    var rmr: Double = 0.0,

    // Total Daily Energy Expenditure
    var caloriesBudget: Double = 0.0,

    val weightDataId: String? = null,

    val dietDataId: String? = null,

    val exerciseDataId: String? = null,

    val sleepDataId: String? = null,
)
