package com.cs125.anappleaday.data.sql.models.user

import androidx.room.Embedded
import androidx.room.Relation
import com.cs125.anappleaday.data.sql.models.health.HealthPlan

data class ProfileWithHealthPlans(
    @Embedded val profile: Profile,
    @Relation(
        parentColumn = "pid",
        entityColumn = "profileId"
    )
    val healthPlans : List<HealthPlan>
)
