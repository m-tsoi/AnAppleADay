package com.cs125.anappleaday.sql.models

import androidx.room.Embedded
import androidx.room.Relation

data class UserAndProfile(
    @Embedded val user: User,
    @Relation(
        parentColumn = "uid",
        entityColumn = "userId"
    ) val profile: Profile
)
