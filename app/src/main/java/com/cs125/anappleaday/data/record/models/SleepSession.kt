package com.cs125.anappleaday.data.record.models

import java.util.Date

data class SleepSession(
    val id: String,
    var startTime: Long,
    var endTime: Long,
    var recomEndTime: Long,
    var date: Date
)
