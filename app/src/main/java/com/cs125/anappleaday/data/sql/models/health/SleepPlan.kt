package com.cs125.anappleaday.data.sql.models.health

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.material.color.utilities.Score
import java.sql.Time
import java.util.UUID

@Entity
data class SleepPlan (
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    val sleepDataId: String,

    val expectedDuration: Double,

    val expectedWakeUpTime: Time,

    val sleepScore: Score,
) {
    companion object {
        const val MAX_HOUR_SLEEP = 9
        const val MIN_HOUR_SLEEP = 7
    }
}
