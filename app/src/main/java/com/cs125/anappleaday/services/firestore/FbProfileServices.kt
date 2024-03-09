package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.record.models.user.Profile
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FbProfileServices (firestore: FirebaseFirestore):
    FbBaseServices<Profile>(firestore = firestore, _collectionName = "Profile"){

    suspend fun getProfile(uid: String): Profile? {

        return try {
            val document = collectionRef.document(uid).get().await()
            document.toObject<Profile>()
        } catch (e: Exception){
            Log.e(TAG, "Failed to retrieve profile", e)
            null
        }
//        var profile: Profile? = null
//        collectionRef.document(uid).get()
//            .addOnSuccessListener {
//                document -> profile = document.toObject(Profile::class.java)
//            }.addOnFailureListener { exception ->
//                Log.e(TAG, "Failed to retrieve profile", exception)
//            }
//
//        return profile
    }

    fun createProfile(uid: String, _profile: Profile): Task<Void> {
        return collectionRef.document(uid).set(
            hashMapOf(
                "age" to _profile.age,
                "gender" to _profile.gender.toString(),
                "height" to _profile.height,
                "weight" to _profile.weight,
                "personicleId" to UUID.randomUUID().toString(),
                "medicalRecords" to mapOf<String, List<String>>(
                    "allergies" to ArrayList(),
                    "foodRestriction" to ArrayList(),
                    "disorders" to ArrayList()
                )
            )
        )
    }

    fun updateProfile(uid: String, _profile: Profile): Task<Void> {
        return collectionRef.document(uid).update(
            hashMapOf(
                "age" to _profile.age,
                "gender" to _profile.gender.toString(),
                "height" to _profile.height,
                "weight" to _profile.weight,
                "personicleId" to _profile.personicleId,
                "medicalRecords" to mapOf(
                    "allergies" to _profile.medicalRecords.allergies,
                    "foodRestriction" to _profile.medicalRecords.foodRestriction,
                    "disorders" to _profile.medicalRecords.disorders
                )
            )
        )
    }
}