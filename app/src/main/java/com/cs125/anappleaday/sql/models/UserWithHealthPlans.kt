package com.cs125.anappleaday.sql.models

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithHealthPlans(
    @Embedded val user: User,
    @Relation(
        parentColumn = "uid",
        entityColumn = "userId"
    ) val healthPlans : List<HealthPlan>
)
