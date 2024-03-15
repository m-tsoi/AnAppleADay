package com.cs125.anappleaday.data.recycler

data class DietViewItem(
    val name: String,
    val deleteMeal: () -> Unit
)
