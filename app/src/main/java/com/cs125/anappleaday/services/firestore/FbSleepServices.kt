package com.cs125.anappleaday.services.firestore

import com.cs125.anappleaday.data.record.models.live.SleepData
import com.google.firebase.firestore.FirebaseFirestore


class FbSleepServices(firestore: FirebaseFirestore) : FbBaseServices<SleepData>(
    "SleepData", firestore) {
    // Note: add functions if needed
}