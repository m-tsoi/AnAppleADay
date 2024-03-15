package com.cs125.anappleaday.data.recycler

data class DietResultItem(
    val name: String,
    val addMeal: () -> Unit
)
