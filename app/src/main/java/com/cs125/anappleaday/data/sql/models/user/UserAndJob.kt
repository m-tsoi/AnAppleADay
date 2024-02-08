package com.cs125.anappleaday.data.sql.models.user

import androidx.room.Embedded
import androidx.room.Relation

data class UserAndJob(
    @Embedded val user: User,
    @Relation(
        parentColumn = "uid",
        entityColumn = "userId"
    )
    val job: Job
)
