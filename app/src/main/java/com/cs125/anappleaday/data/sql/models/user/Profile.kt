package com.cs125.anappleaday.data.sql.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cs125.anappleaday.data.enumTypes.Gender
import java.util.UUID

@Entity
data class Profile(
    @PrimaryKey val pid: UUID,

    var name: String?,

    var avatar: String?,             // image file path

    var age: Int,

    var gender: Gender,

    var personicleId: String,       // pointer to its collection stored in local/cloud

    var medicalRecordId: String    // pointer to its collection stored in local/cloud
)