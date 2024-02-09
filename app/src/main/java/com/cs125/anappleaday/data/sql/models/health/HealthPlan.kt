package com.cs125.anappleaday.data.sql.models.health

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cs125.anappleaday.data.enum.HealthGoal
import com.cs125.anappleaday.data.sql.models.user.User
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(
        entity = User::class,
        parentColumns = ["uid"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class HealthPlan(
    @ColumnInfo(name = "hpId")
    @PrimaryKey val id: UUID = UUID.randomUUID(),

    val userId: String,

    val title: String?,

    val goal: HealthGoal,
)
