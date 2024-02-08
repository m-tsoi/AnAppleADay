package com.cs125.anappleaday.data.sql.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cs125.anappleaday.data.enum.JobType
import java.util.UUID

@Entity
data class Job(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    val title: String?,

    val type: JobType,  // Student as Default

    val hoursPerWeek: Int
)
