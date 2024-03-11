package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.record.models.user.Personicle
import com.cs125.anappleaday.data.record.models.user.Profile
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FbPersonicleServices (firestore: FirebaseFirestore) :
    FbBaseServices<Profile>(firestore = firestore, _collectionName = "Personicle") {

    suspend fun getPersonicle(id: String): Personicle? {
        return try {
            val document = super.getDocument(id).await()
            document.toObject<Personicle>()
        } catch (e: Exception) {
            Log.e(TAG + "Personicle", "${e.message}")
            null
        }
    }


    fun createPersonicle(personicleId: String, personicle: Personicle): Task<Void> {
        return collectionRef.document(personicleId).set(personicle)
    }

    fun updatePersonicle(id: String, dataToUpdate: HashMap<String, Any>): Task<Void> {
        return collectionRef.document(id).update(dataToUpdate)
    }
}