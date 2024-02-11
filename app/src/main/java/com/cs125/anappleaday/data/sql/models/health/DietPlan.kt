package com.cs125.anappleaday.data.sql.models.health

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(entity = HealthPlan::class,
        parentColumns = ["hpId"],
        childColumns = ["healthPlanId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["healthPlanId"])]
)
data class DietPlan(
    @PrimaryKey val id: UUID = UUID.randomUUID(),

    val healthPlanId: String,

    // TODO: To be determined
)
