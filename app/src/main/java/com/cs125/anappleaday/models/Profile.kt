package com.cs125.anappleaday.models

import android.health.connect.datatypes.units.Length
import android.health.connect.datatypes.units.Mass
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cs125.anappleaday.models.enum.Gender


@Entity
data class Profile(
    @PrimaryKey val pid: String,

    val avatar: String,             // image file path

    val age: Int,

    val gender: Gender,

    val height: Length,

    val weight: Mass,               // current weight

    @ColumnInfo(name = "pnid")
    val personicleID: String,       // pointer to its collection stored in local/cloud

    @ColumnInfo(name = "medid")
    val medicalRecordID: String     // pointer to its collection stored in local/cloud
)