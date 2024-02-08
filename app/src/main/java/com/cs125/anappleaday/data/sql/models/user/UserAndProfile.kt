package com.cs125.anappleaday.data.sql.models.user

import androidx.room.Embedded
import androidx.room.Relation
import com.cs125.anappleaday.data.sql.models.user.Profile
import com.cs125.anappleaday.data.sql.models.user.User

data class UserAndProfile(
    @Embedded val user: User,
    @Relation(
        parentColumn = "uid",
        entityColumn = "userId"
    )
    val profile: Profile
)
