package com.cs125.anappleaday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.enumTypes.HealthGoal
import com.cs125.anappleaday.data.record.models.healthPlans.HealthPlan
import com.cs125.anappleaday.databinding.ActivitySelectPlanBinding
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbHealthPlanServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.cs125.anappleaday.services.wellness.InitHealthDataServices
import com.cs125.anappleaday.utils.StatCalculator
import com.google.android.material.card.MaterialCardView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import java.util.Date

class SelectPlanActivity : AppCompatActivity() {
    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var initHealthDataServices: InitHealthDataServices
    private lateinit var healthPlanServices: FbHealthPlanServices
    private lateinit var binding: ActivitySelectPlanBinding

    private var healthPlan: HealthPlan? = null

    // UI components
    private lateinit var healthPlanSelectionLayout: LinearLayout
    private lateinit var healthPlanResultLayout: ConstraintLayout

    // Health Plan Card
    private lateinit var healthPlanCard: MaterialCardView
    private lateinit var goalTextView: TextView
    private lateinit var startDateTextView: TextView
    private lateinit var endDateTextView: TextView

    // Diet Card
    private lateinit var caloriesIntakeTextView: TextView
    private lateinit var proteinIntakeTextView: TextView
    private lateinit var carbIntakeTextView: TextView
    private lateinit var fatIntakeTextView: TextView
    private lateinit var limitedFoodsTextView: TextView
    private lateinit var supportedNutrientsTextView: TextView

    // Exercise Card
    private lateinit var exerciseLevelTextView: TextView
    private lateinit var dailyExerciseDuration: TextView
    private lateinit var suggestedExercise: TextView

    // Sleep Card
    private lateinit var sleepDurationTextView: TextView
    private lateinit var wakeUpTimeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_plan)

        // Init firebase
        fbAuth = FBAuth()
        profileServices = FbProfileServices(Firebase.firestore)
        initHealthDataServices = InitHealthDataServices(Firebase.firestore)
        healthPlanServices = FbHealthPlanServices(Firebase.firestore)

        binding = ActivitySelectPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // UI components
        healthPlanSelectionLayout = findViewById(R.id.health_plan_selection_layout)
        healthPlanResultLayout = findViewById(R.id.health_plan_result)

        // Health Plan Card
        healthPlanCard = findViewById(R.id.health_plan_card)
        goalTextView = findViewById(R.id.goalTextView)
        startDateTextView = findViewById(R.id.startDate)
        endDateTextView = findViewById(R.id.endDate)

        // Diet Card
        caloriesIntakeTextView = findViewById(R.id.day_calories_intake)
        proteinIntakeTextView = findViewById(R.id.day_protein_intake)
        carbIntakeTextView = findViewById(R.id.day_carb_intake)
        fatIntakeTextView = findViewById(R.id.day_fat_intake)
        limitedFoodsTextView = findViewById(R.id.limited_foods)
        supportedNutrientsTextView = findViewById(R.id.supported_nutrients)

        // Exercise Card
        exerciseLevelTextView = findViewById(R.id.exercise_level)
        dailyExerciseDuration = findViewById(R.id.daily_exercise_duration)
        suggestedExercise = findViewById(R.id.suggested_exercises)

        // Sleep Card
        sleepDurationTextView = findViewById(R.id.sleep_duration)
        wakeUpTimeTextView = findViewById(R.id.wake_up_time)

        // Confirm button functionalities
        submitSelectedHealthPlan()
    }

    fun submitSelectedHealthPlan() {
        findViewById<Button>(R.id.confirm_button)
            .setOnClickListener{
                val healthGoal = HealthGoal.getValueOfName(binding.selectedHealthPlan.toString())
                val userId = fbAuth.getUser()?.uid

                lifecycleScope.launch {

                    if (userId != null) {
                        val profile = profileServices.getProfile(userId)
                        if (profile != null) {
                            val rmr = StatCalculator.computeRMR(
                                age = profile.age,
                                gender = profile.gender,
                                height = profile.height.toDouble(),
                                weight = profile.weight.toDouble()
                            )

                            initHealthDataServices.initData(
                                healthGoal = healthGoal,
                                rmr = rmr,
                                startDate = 10000,
                                endDate = 10000,
                            ).addOnSuccessListener {
                                profileServices.updateProfile(
                                    userId,
                                    hashMapOf("healthPlanId" to it.id)
                                )
                                healthPlanSelectionLayout.visibility = View.GONE
                                Toast.makeText(this@SelectPlanActivity,
                                    "Health plan was created.", Toast.LENGTH_SHORT)
                                    .show()
                            }.addOnFailureListener{
                                Toast.makeText(this@SelectPlanActivity,
                                    "Failed to create health plan.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
    }

    fun changeHealthPlan() {
        findViewById<Button>(R.id.change_button).setOnClickListener {
            healthPlanCard.visibility = View.INVISIBLE
        }
    }

    suspend fun pullHealthPlan(id: String) {
        if (healthPlan == null) {
            healthPlan = healthPlanServices.getHealthPLan(id)
        }
    }

    fun renderHealthPlanResult() {
        findViewById<ConstraintLayout>(R.id.health_plan_result)
            .visibility = View.VISIBLE

        if (healthPlan != null) {
            goalTextView.text = "< Goal: ${healthPlan!!.healthGoal} >"
            startDateTextView.text = "${Date(healthPlan!!.startDate)}"
            startDateTextView.text = "${Date(healthPlan!!.endDate)}"

            caloriesIntakeTextView.text = "${healthPlan!!.dietPlan.dayCaloriesIntake} (cal)"
            proteinIntakeTextView.text = "${healthPlan!!.dietPlan.dayProteinIntake * 100}%"
            carbIntakeTextView.text = "${healthPlan!!.dietPlan.dayCarbIntake * 100}%"
            fatIntakeTextView.text = "${healthPlan!!.dietPlan.dayFatIntake * 100}%"
            limitedFoodsTextView.text = toStringCommaList(
                healthPlan!!.dietPlan.limitedFoods
            )
            supportedNutrientsTextView.text = toStringCommaList(
                healthPlan!!.dietPlan.supportedNutrients
            )

            exerciseLevelTextView.text = "${healthPlan!!.exercisePlan.exerciseType}"
            dailyExerciseDuration.text = "${healthPlan!!.exercisePlan.dailyDuration}"
            suggestedExercise.text = toStringCommaList(
                healthPlan!!.exercisePlan.suggestedExercises
            )

            sleepDurationTextView.text = "${healthPlan!!.sleepPlan.sleepDuration} hrs"
            wakeUpTimeTextView.text = "${healthPlan!!.sleepPlan.wakeupTime} am"


        } else {
            Toast.makeText(this, "Health Plan is not avaiable.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun toStringCommaList(mutableList: MutableList<String>): String {
        return mutableList.joinToString(", ") { it + "," }
    }
}