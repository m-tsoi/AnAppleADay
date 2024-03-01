package com.cs125.anappleaday.services.firestore

import com.cs125.anappleaday.data.record.models.live.DietData
import com.google.firebase.firestore.FirebaseFirestore

class FbDietServices(firestore: FirebaseFirestore) : FbBaseServices<DietData>(
    "NutritionData", firestore) {
    // Note: add functions if needed
}