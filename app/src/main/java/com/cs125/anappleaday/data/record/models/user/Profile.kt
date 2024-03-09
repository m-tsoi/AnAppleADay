package com.cs125.anappleaday.data.record.models.user

import com.cs125.anappleaday.data.enumTypes.Gender

data class Profile(
    var age: Int = 0,

    var gender: Gender = Gender.FEMALE,

    // Current height (in.)
    var height: Int = 0,

    // Current weight (lbs)
    var weight: Int = 0,

    var personicleId: String = "",      // pointer to its collection stored in local/cloud

    var healthPlanId: String? = null,

    var medicalRecords: MedicalRecord = MedicalRecord()
)