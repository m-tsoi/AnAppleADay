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
data class SleepPlan (
    @PrimaryKey
    val id: UUID,

    val healthPlanId: String,

    var expectedDuration: Double,   // (hours)

    var expectedWakeUpTime: Long,
) {
    companion object {
        const val MAX_HOUR_SLEEP = 9
        const val MIN_HOUR_SLEEP = 7
    }
}
