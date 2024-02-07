package com.cs125.anappleaday.sql.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User(
    @PrimaryKey val uid: String,

    val username: String,
    val email: String,
)
