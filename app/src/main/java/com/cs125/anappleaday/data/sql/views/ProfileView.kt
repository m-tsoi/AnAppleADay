package com.cs125.anappleaday.data.sql.views

import androidx.room.DatabaseView

@DatabaseView("SELECT Profile.avatar, Profile.age, Profile.gender, " +
        "Profile.personicleId, Profile.medicalRecordId, " +
        "Job.title, Job.type, Job.hoursPerWeek FROM Profile " +
        "INNER JOIN Job ON Profile.pid = Job.profileId")
data class ProfileView(
    val pid: String,
    val avatar: String,
    val age: Int,
    val gender: String,
    val personicleId: String,
    val medicalRecordId: String,
    val jobTitle: String,
    val jobType: String,
    val jobHoursPerWeek: Int
)
