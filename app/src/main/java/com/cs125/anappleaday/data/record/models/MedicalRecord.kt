package com.cs125.anappleaday.data.record.models

data class MedicalRecord(
    val id: String,

    val allergies: MutableList<String>,

    val foodRestrictions: MutableList<String>,

    val disorders: MutableList<String>
)
