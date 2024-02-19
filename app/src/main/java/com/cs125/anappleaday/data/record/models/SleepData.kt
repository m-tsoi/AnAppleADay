package com.cs125.anappleaday.data.record.models

import android.health.connect.datatypes.SleepSessionRecord

data class SleepData(
    val id: String,

    val scores: MutableList<ScoreDay>,

    val sleepSessionRecords: MutableList<SleepSessionRecord>
)
