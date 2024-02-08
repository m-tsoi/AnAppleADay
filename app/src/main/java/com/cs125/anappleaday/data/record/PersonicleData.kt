package com.cs125.anappleaday.data.record

import android.health.connect.datatypes.BodyFatRecord
import android.health.connect.datatypes.TotalCaloriesBurnedRecord
import android.health.connect.datatypes.WeightRecord
import android.health.connect.datatypes.units.Length
import android.health.connect.datatypes.units.Mass
import com.google.android.material.color.utilities.Score

data class PersonicleData(
    val id: String,

    val healthScore: Score,

    val height: Length,

    val weight: Mass,   // current weight

    val bmi: Double,    // Body Mass Index

    val rmr: Double,    // Rest Metabolic Rate

    val bodyCalories: Double,

    val bodyFat: Double,

    val weightRecords: List<WeightRecord>,

    val bodyFatRecords: List<BodyFatRecord>,

    val totalCaloriesBurnedRecords: List<TotalCaloriesBurnedRecord>
)
