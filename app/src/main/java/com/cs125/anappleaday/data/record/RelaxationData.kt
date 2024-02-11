package com.cs125.anappleaday.data.record

data class RelaxationData(
    val id: String,

    val scoreData: ScoreData,

    val relaxationRecords: List<RelaxationRecord>
)
