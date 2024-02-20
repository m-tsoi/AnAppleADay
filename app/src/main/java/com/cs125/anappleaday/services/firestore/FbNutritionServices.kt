package com.cs125.anappleaday.services.firestore

import com.cs125.anappleaday.data.record.models.NutritionData
import com.google.firebase.firestore.FirebaseFirestore

class FbNutritionServices(firestore: FirebaseFirestore) : FbBaseServices<NutritionData>(
    "NutritionData", firestore) {
    // TODO: override functions if needed
}