package com.cs125.anappleaday.data.sql.views

import androidx.room.DatabaseView

@DatabaseView("SELECT * FROM Profile")
data class ProfileView(
    val pid: String,
    val avatar: String,
    val age: Int,
    val gender: String,
    val personicleId: String,
    val medicalRecordId: String,
)