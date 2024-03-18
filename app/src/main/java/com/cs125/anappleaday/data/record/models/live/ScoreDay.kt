package com.cs125.anappleaday.data.record.models.live

import java.util.Date

data class ScoreDay(
    val date: String,
    val score: Double
) {
    constructor(): this("", 0.0)
}