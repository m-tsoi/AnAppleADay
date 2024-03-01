package com.cs125.anappleaday.services.firestore

import com.cs125.anappleaday.data.record.models.user.MedicalRecord
import com.google.firebase.firestore.FirebaseFirestore

class FbMedicalRecordServices(firestore: FirebaseFirestore) : FbBaseServices<MedicalRecord>(
    "NutritionData", firestore) {
    // Note: add functions if needed
}