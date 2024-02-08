package com.cs125.anappleaday.data.sql.models.health

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cs125.anappleaday.data.enum.HealthGoal
import java.util.UUID

@Entity
data class HealthPlan(
    @ColumnInfo(name = "hpId")
    @PrimaryKey val id: UUID = UUID.randomUUID(),

    val userId: String,

    val title: String?,

    val goal: HealthGoal,
)
