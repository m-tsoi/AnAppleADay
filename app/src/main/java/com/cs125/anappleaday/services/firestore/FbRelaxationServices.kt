package com.cs125.anappleaday.services.firestore

import com.cs125.anappleaday.data.record.models.RelaxationData
import com.google.firebase.firestore.FirebaseFirestore

class FbRelaxationServices(firestore: FirebaseFirestore) : FbBaseServices<RelaxationData>(
    "RelaxationData", firestore) {
    // TODO: override functions if needed
}