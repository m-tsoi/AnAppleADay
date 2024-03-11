package com.cs125.anappleaday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.enumTypes.HealthGoal
import com.cs125.anappleaday.data.record.models.healthPlans.HealthPlan
import com.cs125.anappleaday.databinding.ActivitySelectPlanBinding
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbHealthPlanServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import java.util.UUID

class SelectPlanActivity : AppCompatActivity() {
    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var healthPlanServices: FbHealthPlanServices
    private lateinit var binding: ActivitySelectPlanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_plan)

        // Init firebase
        fbAuth = FBAuth()
        profileServices = FbProfileServices(Firebase.firestore)
        healthPlanServices = FbHealthPlanServices(Firebase.firestore)

        binding = ActivitySelectPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Confirm button functionalities
        findViewById<Button>(R.id.confirm_button)
            .setOnClickListener{
                val healthGoal = HealthGoal.getValueOfName(binding.selectedHealthPlan.toString())
                 val userId = fbAuth.getUser()?.uid
                lifecycleScope.launch {
                    if (userId != null) {
                    val healthPlan = HealthPlan(
                        healthGoal = healthGoal,
                    )
                    healthPlanServices.createHealthPlan(healthPlan).addOnSuccessListener {
                        profileServices.updateProfile(
                            UUID.randomUUID().toString(),
                            hashMapOf("healthPlanId" to it.id)
                        )}
                    }

                    Toast.makeText(this@SelectPlanActivity,
                        "Health Plan was created.", Toast.LENGTH_SHORT)
                        .show()
                }
                // TODO: test the update and healthPlan creation
                // TODO: add personicle generator
                // TODO: add advise module to generate health plan
                 startActivity(Intent(this, HomeActivity::class.java))
            }
    }
}