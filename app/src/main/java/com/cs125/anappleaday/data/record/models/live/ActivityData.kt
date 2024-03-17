package com.cs125.anappleaday.data.record.models.live

import android.health.connect.datatypes.ExerciseSessionRecord

data class ActivityData(
    val scores: MutableList<ScoreDay> = mutableListOf(),

    val activities: MutableList<ExerciseSessionRecord> = mutableListOf(),
)
