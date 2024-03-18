package com.cs125.anappleaday.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.record.models.healthPlans.HealthPlan
import com.cs125.anappleaday.data.record.models.live.WeightRecord
import com.cs125.anappleaday.databinding.ActivityWeightTrackerBinding
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbHealthPlanServices
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.cs125.anappleaday.services.firestore.FbWeightServices
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class WeightTrackerActivity: AppCompatActivity() {

    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var personicleServices: FbPersonicleServices
    private lateinit var healthPlanServices: FbHealthPlanServices
    private lateinit var weightServices: FbWeightServices
    private var weightDataId: String? = null
    private var pastWeightRecords: MutableList<WeightRecord>? = null
    private var healthPlan: HealthPlan? = null
    private var startingWeight = 0

    private lateinit var binding: ActivityWeightTrackerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_tracker)

        // Init firebase
        fbAuth = FBAuth()
        profileServices = FbProfileServices(Firebase.firestore)
        healthPlanServices = FbHealthPlanServices(Firebase.firestore)
        personicleServices = FbPersonicleServices(Firebase.firestore)
        weightServices = FbWeightServices(Firebase.firestore)

        binding = ActivityWeightTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.weightInputButton.setOnClickListener {
            addWeightRecord()
        }
    }

    fun addWeightRecord() {
        val weight = binding.weightInput.text.toString().toInt()
        if (weightDataId != null) {
            weightServices.addWeight(weightDataId!!, weight)
                .addOnSuccessListener {
                    Toast.makeText(this, "Weight record was added", Toast.LENGTH_SHORT)
                        .show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to add weight record.", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    override fun onStart() {
        super.onStart()
        val userId = fbAuth.getUser()?.uid
        lifecycleScope.launch {
            val profile = profileServices.getProfile(userId!!)
            if (profile != null) {
                startingWeight = profile.weight
                if (profile.healthPlanId != null)       {
                    healthPlan = healthPlanServices.getHealthPLan(profile.healthPlanId!!)
                }

                
                val personicle = personicleServices.getPersonicle(profile.personicleId)

                if (personicle != null) {
                    weightDataId = personicle.weightDataId
                    if (weightDataId != null) {
                        pastWeightRecords =  weightServices.getPastWeightRecords(id = weightDataId!!)
                    }
                }
            }
        }
    }
}