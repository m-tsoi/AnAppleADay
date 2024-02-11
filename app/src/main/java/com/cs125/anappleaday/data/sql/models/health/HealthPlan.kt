package com.cs125.anappleaday.data.sql.models.health

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cs125.anappleaday.data.enumTypes.HealthGoal
import com.cs125.anappleaday.data.sql.models.user.Profile
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(
        entity = Profile::class,
        parentColumns = ["pid"],
        childColumns = ["profileId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["profileId"])]
)
data class HealthPlan(
    @ColumnInfo(name = "hpId")
    @PrimaryKey val id: UUID,

    val profileId: String,

    var title: String,

    var goal: HealthGoal,
)
