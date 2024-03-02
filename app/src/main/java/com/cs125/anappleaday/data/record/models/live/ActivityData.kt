package com.cs125.anappleaday.data.record.models.live

import android.health.connect.datatypes.ExerciseSessionRecord

data class ActivityData(
    val id: String,

    val scores: MutableList<ScoreDay>,

    val activities: MutableList<ExerciseSessionRecord>,
)
