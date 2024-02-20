package com.cs125.anappleaday.services.firestore

import com.cs125.anappleaday.data.record.models.SleepData
import com.google.firebase.firestore.FirebaseFirestore


class FbSleepServices(firestore: FirebaseFirestore) : FbBaseServices<SleepData>(
    "SleepData", firestore) {
    // TODO: override functions if needed
}