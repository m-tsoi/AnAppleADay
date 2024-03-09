package com.cs125.anappleaday.data.record.models.user

data class MedicalRecord(
    val allergies: MutableList<String> = mutableListOf(),

    val foodRestriction: MutableList<String> = mutableListOf(),

    val disorders: MutableList<String> = mutableListOf()
)
