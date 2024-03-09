package com.cs125.anappleaday.data.record.models.healthPlans

import com.cs125.anappleaday.data.enumTypes.HealthGoal

data class HealthPlan (
    val healthGoal: HealthGoal = HealthGoal.BE_HEALTHY,

    var score: Double = 0.0,

    val dietPlan: DietPlan = DietPlan(),

    val exercisePlan: ExercisePlan = ExercisePlan(),

    val relaxationPlan: RelaxationPlan = RelaxationPlan(),

    val sleepPlan: SleepPlan = SleepPlan(),
)
