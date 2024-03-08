package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

open class FbBaseServices <T> (
    _collectionName: String,
    firestore: FirebaseFirestore,
) {
    private val collectionName = _collectionName
    var collectionRef: CollectionReference

    init {
        collectionRef = firestore.collection(collectionName)
    }

    open fun <T : Any> getDocument(id: String) : Task<DocumentSnapshot> {
        return collectionRef.document(id).get().addOnSuccessListener {
            Log.d("$TAG: $collectionName", "document saved successfully")
            return@addOnSuccessListener
        }.addOnFailureListener{
            Log.d("$TAG: $collectionName", "failed to save document")
            return@addOnFailureListener
        }
    }

    open fun <T : Any> add(data: T): Task<DocumentReference> {
        return collectionRef.add(data).addOnSuccessListener {
            Log.d("$TAG: $collectionName", "document saved successfully")
            return@addOnSuccessListener
        }.addOnFailureListener{
            Log.d("$TAG: $collectionName", "failed to save document")
            return@addOnFailureListener
        }
    }

    open fun <T : Any> update(id: String, data: T): Task<Void> {
        return collectionRef.document(id).set(data, SetOptions.merge()).addOnSuccessListener {
            Log.d("$TAG: $collectionName", "document updated successfully")
            return@addOnSuccessListener
        }.addOnFailureListener{
            Log.d("$TAG: $collectionName", "failed to update document")
            return@addOnFailureListener
        }
    }

    open fun delete(id: String) {
        collectionRef.document(id).delete().addOnSuccessListener {
            Log.d("$TAG: $collectionName", "document deleted successfully")
            return@addOnSuccessListener
        }.addOnFailureListener{
            Log.d("$TAG: $collectionName", "failed to delete document")
            return@addOnFailureListener
        }
    }

    companion object {
        val TAG = "Collection"
    }
}