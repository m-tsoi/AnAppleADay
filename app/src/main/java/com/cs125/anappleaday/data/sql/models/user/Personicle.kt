package com.cs125.anappleaday.data.sql.models.user

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(foreignKeys = [ForeignKey(entity = Profile::class,
    parentColumns = ["pid"],
    childColumns = ["profileId"],
    onDelete = ForeignKey.CASCADE
)],
    indices = [Index(value = ["profileId"])])
data class Personicle(
    @PrimaryKey
    val id: UUID,

    val profileId: String,

    var healthScore: Double,

    // Current height (in.)
    var height: Double,

    // Current weight (lbs)
    var weight: Double,

    // Body Mass Index
    var bmi: Double,

    // Rest Metabolic Rate
    var rmr: Double,

    // Calories requires for the current body
    var bodyCalories: Double,

    val weightRecordsId: String?,

    val dietDataId: String?,

    val activityDataId: String?,

    val relaxationDataId: String?,

    val sleepDataId: String?,
)
