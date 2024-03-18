package com.cs125.anappleaday.data.record.models.live

data class SleepData(
    val dailySleepRecords: MutableList<SleepSession> = mutableListOf<SleepSession>()


)
