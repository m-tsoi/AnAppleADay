package com.cs125.anappleaday.data.record.models.live

data class RelaxationData(
    val id: String,

    val scores: MutableList<ScoreDay>,

    val relaxationRecords: MutableList<RelaxationRecord>
)
