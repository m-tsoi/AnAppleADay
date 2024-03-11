package com.cs125.anappleaday.data.enumTypes

enum class HealthGoal(val displayName: String) {
    BE_HEALTHY("Be Healthy"),
    STAY_LEAN("Stay Lean"),
    BULK_UP("Bulk Up"),
    LOSE_FAT("Lose Fat");

    companion object {
        fun getValueOfName(_displayName: String): HealthGoal {
            return entries.firstOrNull { it.displayName == _displayName } ?: BE_HEALTHY
        }
    }
}

