package com.cs125.anappleaday.data.record.models.healthPlans

import com.cs125.anappleaday.data.enumTypes.ActivityLevel

data class ExercisePlan(
    var exerciseType: ActivityLevel = ActivityLevel.MODERATE,

    val suggestedExercises: MutableList<String> = mutableListOf(),

    var dailyDuration: Double = 0.0 // hours
)
