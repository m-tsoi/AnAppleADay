package com.cs125.anappleaday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.cs125.anappleaday.R
import com.cs125.anappleaday.databinding.ActivityLoginBinding
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbHealthPlanServices
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var fbAuth: FBAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var profileService: FbProfileServices
    private lateinit var healthPlanServices: FbHealthPlanServices
    private lateinit var personicleServices: FbPersonicleServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        fbAuth = FBAuth()
        val db = Firebase.firestore
        profileService = FbProfileServices(db)
        healthPlanServices = FbHealthPlanServices(db)
        personicleServices = FbPersonicleServices(db)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Login button functionalities
        findViewById<Button>(R.id.login_button)
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")

                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast
                        .makeText(this, "Field cannot be blank", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    fbAuth.login(email, password).addOnCompleteListener{
                        if (!it.isSuccessful) {
                            Toast.makeText(this, "Incorrect email or password.",
                                    Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this, "Access granted.",
                                Toast.LENGTH_SHORT)
                                .show()
                            lifecycleScope.launch {
                                val userId = fbAuth.getUser()?.uid
                                redirectNotCreated(userId)
                            }
                        }
                    }
                }
            }

        // Register button functionalities
        findViewById<Button>(R.id.register_button)
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                finish()
                val sendIntent = Intent(this, RegisterActivity::class.java)
                startActivity(sendIntent)
            }
    }

    override fun onStart() {
        super.onStart()
        if(fbAuth.isCurrentUser()){
            val userId = fbAuth.getUser()?.uid
            redirectNotCreated(userId)
        }
    }

    private fun redirectNotCreated(userId: String? = null) {
        if (userId == null) return
        lifecycleScope.launch {
            val profile = profileService.getProfile(userId)
            if (profile == null) {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        ProfileCreationActivity::class.java
                    )
                )
            } else {
                if (personicleServices.getPersonicle(profile.personicleId) == null) {
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            InitPersonicleActivity::class.java
                        )
                    )
                } else if (profile.healthPlanId?.let { it1 ->
                        healthPlanServices.getHealthPLan(it1)
                    } == null) {
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            SelectPlanActivity::class.java
                        )
                    )
                } else {
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            HomeActivity::class.java
                        )
                    )
                }
            }
        }
    }
}