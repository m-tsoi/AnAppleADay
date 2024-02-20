package com.cs125.anappleaday.services.firestore

import com.cs125.anappleaday.data.record.models.MedicalRecord
import com.google.firebase.firestore.FirebaseFirestore

class FbMedicalRecordServices(firestore: FirebaseFirestore) : FbBaseServices<MedicalRecord>(
    "NutritionData", firestore) {
    // TODO: override functions if needed
}