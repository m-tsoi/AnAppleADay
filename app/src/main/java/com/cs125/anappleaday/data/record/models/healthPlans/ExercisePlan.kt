package com.cs125.anappleaday.data.record.models.healthPlans

import com.cs125.anappleaday.data.enumTypes.ActivityLevel

data class ExercisePlan(
    var exerciseType: ActivityLevel = ActivityLevel.MODERATE,

    val suggestExercises: MutableList<String> = mutableListOf(),

    var totalDuration: Double = 0.0
)
