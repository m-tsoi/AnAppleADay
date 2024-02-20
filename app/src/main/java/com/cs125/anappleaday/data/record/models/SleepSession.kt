package com.cs125.anappleaday.data.record.models

import java.time.Instant

data class SleepSession(
    val startTime: Instant,
    val endTime: Instant
)
