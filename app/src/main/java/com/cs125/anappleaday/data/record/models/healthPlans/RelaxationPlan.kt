package com.cs125.anappleaday.data.record.models.healthPlans

data class RelaxationPlan(
    val suggestedActivities: MutableList<String>,

    var suggestedMusic: MutableList<String>,

    var breakTime: Double,
)
