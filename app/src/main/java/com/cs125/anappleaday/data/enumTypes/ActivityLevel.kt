package com.cs125.anappleaday.data.enumTypes

enum class ActivityLevel(val displayName: String, val value: Double) {
    // src "https://www.niddk.nih.gov/bwp"
    // Assuming physical activity level at work for studying is Light
    // This enum indicate physical activity level in leisure time
    VERY_LITTLE("Very Little", 1.5),
    LIGHT("Light", 1.6),
    MODERATE("Moderate", 1.7),
    VERY_ACTIVE("Very Active", 1.8),
    EXTRA_ACTIVE("Extra Active", 2.0);

    companion object {
        fun getValueOfName(_displayName: String): ActivityLevel {
            return ActivityLevel.entries.firstOrNull { it.displayName == _displayName } ?: MODERATE
        }
    }
}