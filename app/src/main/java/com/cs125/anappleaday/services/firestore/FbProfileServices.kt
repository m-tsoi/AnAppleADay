package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.record.models.user.Profile
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class FbProfileServices (firestore: FirebaseFirestore):
    FbBaseServices<Profile>(firestore = firestore, _collectionName = "Profile"){

    fun getProfile(uid: String): Profile? {
        var profile: Profile? = null
        collectionRef.document(uid).get()
            .addOnSuccessListener {
                document ->
                if (document != null) {
                    profile = document.toObject(Profile::class.java)!!
                }
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Failed to retrieve profile", exception)
            }

        return profile
    }

    fun createProfile(uid: String, _profile: Profile): Task<Void> {
        return collectionRef.document(uid).set({
            "age" to _profile.age
            "gender" to _profile.gender.toString()
            "height" to _profile.height
            "weight" to _profile.weight
            "personicleId" to UUID.randomUUID().toString()
            "medicalRecords" to mapOf<String, MutableList<String>>(
                "allergies" to ArrayList(),
                "foodRestriction" to ArrayList(),
                "disorders" to ArrayList()
            )
        })
    }


    fun updateProfile(uid: String, _profile: Profile): Task<Void> {
        return collectionRef.document(uid).update(
            mapOf(
                "age" to _profile.age,
                "gender" to _profile.gender.toString(),
                "height" to _profile.height,
                "weight" to _profile.weight,
                "personicleId" to _profile.personicleId,
                "medicalRecords" to mapOf<String, MutableList<String>>(
                    "allergies" to _profile.medicalRecord.allergies,
                    "foodRestriction" to _profile.medicalRecord.foodRestriction,
                    "disorders" to _profile.medicalRecord.disorders
                )
            )
        )
    }
}