package com.cs125.anappleaday.data.record.models

data class SleepData(
    val id: String,

    val sleepSessionRecords: MutableList<SleepSession>
)
