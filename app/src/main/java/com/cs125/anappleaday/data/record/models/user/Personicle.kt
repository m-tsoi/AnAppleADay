package com.cs125.anappleaday.data.record.models.user

data class Personicle(
    var healthScore: Double = 0.0,

    // Body Mass Index
    var bmi: Double = 0.0,

    // Rest Metabolic Rate
    var rmr: Double = 0.0,

    // Calories requires for the current body
    var bodyCalories: Double = 0.0,

    val weightRecordsId: String? = null,

    val dietDataId: String? = null,

    val activityDataId: String? = null,

    val relaxationDataId: String? = null,

    val sleepDataId: String? = null,
)
