package com.cs125.anappleaday.data.sql.dto

import com.cs125.anappleaday.data.enumTypes.Gender

data class ProfileDto(
    var name: String,

    var avatar: String,             // image file path

    var age: Int,

    var gender: String,
)
