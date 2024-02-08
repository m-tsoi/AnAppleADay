package com.cs125.anappleaday.data.sql.models.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cs125.anappleaday.data.enum.Gender

@Entity
data class Profile(
    @PrimaryKey val pid: String,

    val avatar: String,             // image file path

    val age: Int,

    val gender: Gender,

    @ColumnInfo(name = "pnId")
    val personicleId: String,       // pointer to its collection stored in local/cloud

    @ColumnInfo(name = "medId")
    val medicalRecordId: String     // pointer to its collection stored in local/cloud
)