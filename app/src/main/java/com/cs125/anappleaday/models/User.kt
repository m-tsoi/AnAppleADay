package com.cs125.anappleaday.models
import com.google.android.libraries.healthdata.data.GenderType

data class User(
    val name: String,
    val age: Int,
    val gender: GenderType,
    val weight: Int,
    val height: Int,
    // TODO: add reference keys
)
