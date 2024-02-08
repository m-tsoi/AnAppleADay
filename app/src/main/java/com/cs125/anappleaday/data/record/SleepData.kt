package com.cs125.anappleaday.data.record

import android.health.connect.datatypes.SleepSessionRecord

data class SleepData(
    val id: String,

    val scoreData: ScoreData,

    val sleepSessionRecords: List<SleepSessionRecord>
)
