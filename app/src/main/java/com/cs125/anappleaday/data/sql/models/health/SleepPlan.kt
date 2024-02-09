package com.cs125.anappleaday.data.sql.models.health

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(entity = HealthPlan::class,
        parentColumns = ["hpId"],
        childColumns = ["healthPlanId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SleepPlan (
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    val healthPlanId: String,

    val sleepDataId: String,

    val expectedDuration: Double,

    val expectedWakeUpTime: Time,
) {
    companion object {
        const val MAX_HOUR_SLEEP = 9
        const val MIN_HOUR_SLEEP = 7
    }
}
