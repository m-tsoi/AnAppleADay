package com.cs125.anappleaday.data.sql.views

import androidx.room.DatabaseView

@DatabaseView("SELECT User.username, User.email From User")
data class UserView(
    val username: String,
    val email: String
)
