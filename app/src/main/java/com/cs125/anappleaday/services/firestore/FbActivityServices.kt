package com.cs125.anappleaday.services.firestore

import com.cs125.anappleaday.data.record.models.ActivityData
import com.google.firebase.firestore.FirebaseFirestore

class FbActivityServices (firestore: FirebaseFirestore) : FbBaseServices<ActivityData>(
    "ActivityData", firestore) {
    // TODO: override functions if needed
}