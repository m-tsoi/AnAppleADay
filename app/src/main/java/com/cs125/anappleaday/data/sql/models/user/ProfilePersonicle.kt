package com.cs125.anappleaday.data.sql.models.user

import androidx.room.Embedded
import androidx.room.Relation

data class ProfilePersonicle(
    @Embedded val profile: Profile,
    @Relation(
        parentColumn = "pid",
        entityColumn = "profileId"
    )
    val personicle: Personicle
)
