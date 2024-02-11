package com.cs125.anappleaday.data.record

data class ComfortData(
    val id: String,

    val scoreData: ScoreData,

    val comfortRecords: List<ComfortRecord>
)
