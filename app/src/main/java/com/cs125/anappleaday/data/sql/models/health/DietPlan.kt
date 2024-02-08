package com.cs125.anappleaday.data.sql.models.health

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class DietPlan(
    @PrimaryKey val id: UUID = UUID.randomUUID(),

    val nutritionDataId: String,

    // TODO: To be determined
)
