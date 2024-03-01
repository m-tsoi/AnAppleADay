package com.cs125.anappleaday.data.record.models.user

import com.cs125.anappleaday.data.enumTypes.Gender

data class Profile (
    val id: String,

    var name: String,

    var photoUrl: String,             // image file path

    var age: Int,

    var gender: Gender,

    var personicleId: String,      // pointer to its collection stored in local/cloud

    var medicalRecordId: MedicalRecord    // pointer to its collection stored in local/cloud
)