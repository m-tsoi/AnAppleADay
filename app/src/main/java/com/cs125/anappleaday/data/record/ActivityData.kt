package com.cs125.anappleaday.data.record

import android.health.connect.datatypes.ExerciseSessionRecord

data class ActivityData(
    val id: String,

    val scoreData: ScoreData,

    val activities: List<ExerciseSessionRecord>,
)
