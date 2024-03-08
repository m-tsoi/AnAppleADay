package com.cs125.anappleaday.services.firestore

import com.cs125.anappleaday.data.record.models.live.RelaxationData
import com.google.firebase.firestore.FirebaseFirestore

class FbRelaxationServices(firestore: FirebaseFirestore) : FbBaseServices<RelaxationData>(
    "RelaxationData", firestore) {
    // Note: add functions if needed
}