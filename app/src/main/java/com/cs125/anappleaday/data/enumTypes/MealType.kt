package com.cs125.anappleaday.data.enumTypes

enum class MealType(val displayName: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner");

    companion object {
        fun getValueOfName(_displayName: String): MealType {
            return MealType.entries.firstOrNull { it.displayName == _displayName } ?: LUNCH
        }
    }
}