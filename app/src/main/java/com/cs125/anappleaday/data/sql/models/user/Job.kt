package com.cs125.anappleaday.data.sql.models.user

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cs125.anappleaday.data.enumTypes.JobType
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(
        entity = Profile::class,
        parentColumns = ["pid"],
        childColumns = ["profileId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["profileId"])])
data class Job(
    @PrimaryKey
    val id: UUID,

    val profileId: String,

    var title: String,

    var type: JobType,  // Student as Default

    var hoursPerWeek: Int
)
