package com.cs125.anappleaday.data.sql.models.user

import androidx.room.Embedded
import androidx.room.Relation
import com.cs125.anappleaday.data.sql.models.health.HealthPlan

data class UserWithHealthPlans(
    @Embedded val user: User,
    @Relation(
        parentColumn = "uid",
        entityColumn = "userId"
    )
    val healthPlans : List<HealthPlan>
)
