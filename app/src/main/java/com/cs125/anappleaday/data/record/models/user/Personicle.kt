package com.cs125.anappleaday.data.record.models.user

data class Personicle(
    val profileId: String,

    var healthScore: Double,

    // Body Mass Index
    var bmi: Double,

    // Rest Metabolic Rate
    var rmr: Double,

    // Calories requires for the current body
    var bodyCalories: Double,

    val weightRecordsId: String?,

    val dietDataId: String?,

    val activityDataId: String?,

    val relaxationDataId: String?,

    val sleepDataId: String?,
)
