package com.cs125.anappleaday.data.sql.models.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.cs125.anappleaday.data.enum.Gender

@Entity(foreignKeys = [
    ForeignKey(
        entity = User::class,
        parentColumns = ["uid"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )])
data class Profile(
    @PrimaryKey val pid: String,

    val userId: String,

    val avatar: String,             // image file path

    val age: Int,

    val gender: Gender,

    val personicleId: String,       // pointer to its collection stored in local/cloud

    val medicalRecordId: String     // pointer to its collection stored in local/cloud
)