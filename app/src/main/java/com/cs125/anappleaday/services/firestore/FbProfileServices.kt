package com.cs125.anappleaday.services.firestore

import com.cs125.anappleaday.data.record.models.user.Profile
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FbProfileServices(firestore: FirebaseFirestore)
    : FbBaseServices<Profile>("Profile", firestore) {

    private fun mapProfileToDoc(profile: Profile): MutableMap<String, Any> {
        return mutableMapOf<String, Any>(
            "id" to profile.id,
            "photoUrl" to profile.photoUrl,
            "gender" to profile.gender.toString(),
            "personicleId" to profile.personicleId,
            "medicalRecordId" to profile.medicalRecordId
        )
    }

    fun addProfile(profile: Profile): Task<DocumentReference> {
        val docData = mapProfileToDoc(profile)
        return super.add(docData)
    }

    fun updateProfile(profile: Profile): Task<Void> {
        val docData = mapProfileToDoc(profile)
        return super.update(profile.id.toString(), docData)
    }
}