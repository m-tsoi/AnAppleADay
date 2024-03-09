package com.cs125.anappleaday.data.record.models.healthPlans

data class RelaxationPlan(
    val suggestedActivities: MutableList<String> = mutableListOf(),

    val suggestedMusic: MutableList<String> = mutableListOf(),

    var breakTime: Double = 0.0,
)
