package com.cs125.anappleaday.data.record.models.healthPlans

data class ExercisePlan(
    val exerciseType: String,

    val suggestExercises: MutableList<String>,

    val totalDuration: Double
)
