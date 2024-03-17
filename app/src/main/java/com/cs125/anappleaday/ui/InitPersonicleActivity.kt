package com.cs125.anappleaday.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.enumTypes.ActivityLevel
import com.cs125.anappleaday.data.record.models.live.ActivityData
import com.cs125.anappleaday.data.record.models.live.DietData
import com.cs125.anappleaday.data.record.models.live.SleepData
import com.cs125.anappleaday.data.record.models.user.Personicle
import com.cs125.anappleaday.databinding.ActivityInitPersonicleBinding
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.cs125.anappleaday.utils.StatCalculator
import com.google.android.material.card.MaterialCardView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import java.util.UUID

class InitPersonicleActivity: AppCompatActivity() {
    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var personicleServices: FbPersonicleServices
    private lateinit var binding: ActivityInitPersonicleBinding

    // UI components
    private lateinit var bmiTextView: TextView
    private lateinit var rmrTextView: TextView
    private lateinit var caloriesBudgetTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init firebase
        fbAuth = FBAuth()
        profileServices = FbProfileServices(Firebase.firestore)
        personicleServices = FbPersonicleServices(Firebase.firestore)

        // Init UI component
        binding = ActivityInitPersonicleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bmiTextView = findViewById(R.id.bmiTextView)
        rmrTextView = findViewById(R.id.rmrTextView)
        caloriesBudgetTextView =  findViewById(R.id.caloriesBudgetTextView)

        findViewById<Button>(R.id.confirm_button)
            .setOnClickListener {
                val activityLevel = ActivityLevel.getValueOfName(binding.selectedActivityLevel.toString())
                val userId = fbAuth.getUser()?.uid

                lifecycleScope.launch {
                    if (userId != null) {
                        val profile = profileServices.getProfile(userId)
                        if (profile != null) {
                            // Calculate stats
                            val bmi = StatCalculator.computeBMI(
                                height = profile.height.toDouble(),
                                weight = profile.weight.toDouble()
                            )
                            val rmr = StatCalculator.computeRMR(
                                age = profile.age,
                                gender = profile.gender,
                                height = profile.height.toDouble(),
                                weight = profile.weight.toDouble()
                            )
                            val caloriesBudget = StatCalculator.computeCaloriesBudget(rmr, activityLevel)

                            // Create new blank SleepData, DietData, ActivityData documents in firestore for this user
                            val db = Firebase.firestore
                            val sleepDataDocUUID = UUID.randomUUID().toString()
                            val dietDataDocUUID = UUID.randomUUID().toString()
                            val activityDataDocUUID = UUID.randomUUID().toString()
                            db.collection("SleepData").document(sleepDataDocUUID).set(SleepData())
                            db.collection("DietData").document(dietDataDocUUID).set(DietData())
                            db.collection("ActivityData").document(activityDataDocUUID).set(ActivityData())

                            // Create personicle in firestore
                            personicleServices.createPersonicle(
                                personicleId = profile.personicleId,
                                personicle = Personicle(
                                    healthScore = 0.0,
                                    bmi = bmi,
                                    rmr = rmr ,
                                    caloriesBudget = caloriesBudget,
                                    sleepDataId = sleepDataDocUUID,
                                    dietDataId = dietDataDocUUID,
                                    activityDataId = activityDataDocUUID
                                )
                            ).addOnSuccessListener {
                                Toast.makeText(this@InitPersonicleActivity,
                                    "Personicle was created", Toast.LENGTH_SHORT)
                                    .show()

                                renderPersonicleResult(bmi, rmr, caloriesBudget)
                            }.addOnFailureListener{
                                Toast.makeText(this@InitPersonicleActivity,
                                    "Personicle was created", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }


            findViewById<Button>(R.id.to_health_plan_button).setOnClickListener{
                startActivity(Intent(this, SelectPlanActivity::class.java))
            }
    }

    private fun renderPersonicleResult(bmi: Double, rmr: Double, caloriesBudget: Double) {
        bmiTextView.text = "BMI: ${bmi}"
        rmrTextView.text = "RMR: ${rmr}"
        caloriesBudgetTextView.text = "Calories Budget: ${caloriesBudget}"
        findViewById<MaterialCardView>(R.id.personicle_stats_card)
            .visibility = View.VISIBLE

        findViewById<Button>(R.id.confirm_button)
            .visibility = View.GONE

        findViewById<Button>(R.id.to_health_plan_button)
            .visibility = View.VISIBLE
    }
}