package com.cs125.anappleaday.data.record.models.live

import java.util.Date

data class SleepSession(
    var startTime: Date? = Date(),
    var endTime: Date? = Date(),
    var sleepScore: Int? = 0,
    var recomEndTime: Date? = Date(),
    var enteredDate: Date? = Date()
)
