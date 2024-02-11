package com.cs125.anappleaday.data.record

data class MedicalRecord(
    val id: String,

    val allergies: List<String>,

    val foodRestrictions: List<String>,

    val disorders: List<String>
)
