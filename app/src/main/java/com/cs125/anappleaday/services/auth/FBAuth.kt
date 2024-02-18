package com.cs125.anappleaday.services.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class FBAuth {
    private lateinit var auth: FirebaseAuth

    fun init() {
        auth = Firebase.auth
    }

    fun isCurrentUser(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    fun register(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun logout(email: String, password: String) {
        auth.signOut()
    }
}