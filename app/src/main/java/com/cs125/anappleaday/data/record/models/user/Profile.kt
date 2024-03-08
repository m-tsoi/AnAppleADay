package com.cs125.anappleaday.data.record.models.user

import com.cs125.anappleaday.data.enumTypes.Gender

data class Profile (
    var age: Int,

    var gender: Gender,

    // Current height (in.)
    var height: Int,

    // Current weight (lbs)
    var weight: Int,

    var personicleId: String,      // pointer to its collection stored in local/cloud

    var medicalRecord: MedicalRecord
)