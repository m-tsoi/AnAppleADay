package com.cs125.anappleaday.data.sql.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: String,

    val username: String,

    val email: String,
)
