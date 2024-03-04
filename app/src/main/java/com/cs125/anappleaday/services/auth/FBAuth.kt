package com.cs125.anappleaday.services.auth

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

class FBAuth {
    private var auth: FirebaseAuth = Firebase.auth

    fun isCurrentUser(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    fun getUser(): FirebaseUser? {
        return if (auth.currentUser != null) auth.currentUser else null
    }

    fun updateUser(name: String, photoUrl: String): Task<Void> {
        val profileUpdates = userProfileChangeRequest {
            displayName = name
            photoUri = Uri.parse(photoUrl)
        }

        val user = auth.currentUser
        return user!!.updateProfile(profileUpdates)
    }

    fun register(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun logout() {
        auth.signOut()
    }
}