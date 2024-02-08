package com.cs125.anappleaday.data.sql.models.health

import androidx.room.Embedded
import androidx.room.Relation

data class HealthPlanAndSleepPlan(
    @Embedded val healthPlan: HealthPlan,
    @Relation(
        parentColumn = "hpId",
        entityColumn = "healthPlanId"
    )
    val sleepPlan: SleepPlan
)
