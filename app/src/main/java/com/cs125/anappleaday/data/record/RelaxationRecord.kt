package com.cs125.anappleaday.data.record

import com.cs125.anappleaday.data.enumTypes.StressLevel
import java.util.Date

data class RelaxationRecord(
    val id: String,

    val action: String,

    val stressLevel: StressLevel,

    val date: Date
)
