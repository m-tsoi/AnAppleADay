package com.cs125.anappleaday.data.record.models.user

data class MedicalRecord(
    val allergies: MutableList<String>,

    val foodRestriction: MutableList<String>,

    val disorders: MutableList<String>
)
