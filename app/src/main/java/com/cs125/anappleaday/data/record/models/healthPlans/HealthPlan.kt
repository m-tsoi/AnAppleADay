package com.cs125.anappleaday.data.record.models.healthPlans

import com.cs125.anappleaday.data.enumTypes.HealthGoal

data class HealthPlan (
    val healthGoal: HealthGoal,

    val score: Double,

    val dietPlan: DietPlan,

    val exercisePlan: ExercisePlan,

    val relaxationPlan: RelaxationPlan,

    val sleepPlan: SleepPlan,
)
