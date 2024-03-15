package com.cs125.anappleaday.ui

import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    // Buttons
    private lateinit var changeButton: Button
    private lateinit var confirmButton: Button
    private lateinit var homeButton: Button
    private lateinit var cancelButton: Button

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())



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

        // Buttons
        confirmButton = findViewById(R.id.confirm_button)
        changeButton = findViewById(R.id.change_button)
        cancelButton = findViewById(R.id.cancel_button)
        homeButton = findViewById(R.id.home_button)
        

        // Set listeners
        submitSelectedHealthPlan()
        setDatePickerDialogListeners()
        setChangeHealthPlanListerner()
        setCancelChangeHealthPlanListener()
        setRedirectUIListeners()
    }

    override fun onStart() {
        val userId = fbAuth.getUser()?.uid
        if (userId != null) {
            lifecycleScope.launch {
                healthPlanSelectionLayout.visibility = View.INVISIBLE
                val profile = profileServices.getProfile(userId)
                if (profile?.healthPlanId != null) {
                    fetchHealthPlanResult(profile.healthPlanId!!)
                }

                if (healthPlan == null)
                    healthPlanSelectionLayout.visibility = View.VISIBLE
            }
        }
        super.onStart()
    }

    private suspend fun fetchHealthPlanResult(healthPlanId: String) {
        healthPlan = healthPlanServices.getHealthPLan(healthPlanId)

        if (healthPlan != null) {
            renderHealthPlanResult()
        }
    }

    fun openDatePickerDialog(dateButton: Button) {
        val datePickerDialog = DatePickerDialog(
            this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val formattedDate = dateFormat.format(selectedDate.time)
                dateButton.text = "$formattedDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    fun submitSelectedHealthPlan() {
        findViewById<Button>(R.id.confirm_button)
            .setOnClickListener{
                val healthGoal = HealthGoal.getValueOfName(binding.selectedHealthPlan.text.toString())
                val startDate = binding.startDateButton.text.toString()
                val endDate = binding.endDateButton.text.toString()
                val userId = fbAuth.getUser()?.uid
                
                if (startDate == "" || endDate == "" || startDate > endDate) {
                    Toast.makeText(this@SelectPlanActivity,
                        "Invalid date", Toast.LENGTH_SHORT)
                        .show()
                } else {
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
                                    startDate = startDate,
                                    endDate = endDate,
                                ).addOnSuccessListener {
                                    lifecycleScope.launch {
                                        healthPlanSelectionLayout.visibility = View.GONE
                                        profileServices.updateProfile(
                                            userId,
                                            hashMapOf("healthPlanId" to it.id)
                                        )
                                        fetchHealthPlanResult(it.id)
                                    }
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
    }

    private fun setChangeHealthPlanListerner() {
        changeButton.setOnClickListener {
            healthPlanCard.visibility = View.INVISIBLE
            healthPlanSelectionLayout.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
        }
    }

    private fun setCancelChangeHealthPlanListener() {
        cancelButton.setOnClickListener {
            healthPlanSelectionLayout.visibility = View.GONE
            healthPlanCard.visibility = View.VISIBLE
        }
    }

    private fun setDatePickerDialogListeners() {
        binding.startDateButton.setOnClickListener {
            openDatePickerDialog(binding.startDateButton)
        }

        binding.endDateButton.setOnClickListener {
            openDatePickerDialog(binding.endDateButton)
        }
    }

    private fun setRedirectUIListeners() {
        homeButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }


    private fun renderHealthPlanResult() {
        findViewById<ConstraintLayout>(R.id.health_plan_result)
            .visibility = View.VISIBLE

        if (healthPlanCard.visibility == View.INVISIBLE)
            healthPlanCard.visibility = View.VISIBLE

        if (healthPlan != null) {
            goalTextView.text = "< Goal: ${healthPlan!!.healthGoal.displayName} >"
            startDateTextView.text = healthPlan!!.startDate
            endDateTextView.text = healthPlan!!.endDate

            caloriesIntakeTextView.text = "Daily Calories Intake: ${String.format("%.1f", healthPlan!!.dietPlan.dayCaloriesIntake)} (cal)"
            proteinIntakeTextView.text = "${String.format("%.1f", healthPlan!!.dietPlan.dayProteinIntake * 100)}%"
            carbIntakeTextView.text = "${String.format("%.1f", healthPlan!!.dietPlan.dayCarbIntake * 100)}%"
            fatIntakeTextView.text = "${String.format("%.1f", healthPlan!!.dietPlan.dayFatIntake * 100)}%"
            limitedFoodsTextView.text = "Limited Food: ${toStringCommaList(
                healthPlan!!.dietPlan.limitedFoods
            )}"
            supportedNutrientsTextView.text = "Supported Nutrients: ${toStringCommaList(
                healthPlan!!.dietPlan.supportedNutrients
            )}"

            exerciseLevelTextView.text = healthPlan!!.exercisePlan.exerciseType.displayName
            dailyExerciseDuration.text = convertToHourMinString(healthPlan!!.exercisePlan.dailyDuration)
            suggestedExercise.text = "Suggested exercises: ${toStringCommaList(
                healthPlan!!.exercisePlan.suggestedExercises
            )}"

            sleepDurationTextView.text = "Duration: ${healthPlan!!.sleepPlan.sleepDuration} hrs"
            wakeUpTimeTextView.text = "Wake up at: ${healthPlan!!.sleepPlan.wakeupTime} am"


        } else {
            Toast.makeText(this, "Health Plan is not avaiable.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun toStringCommaList(mutableList: MutableList<String>): String {
        return mutableList.joinToString(", ")
    }

    private fun convertToHourMinString(_hours: Double): String {
        val hours = _hours.toInt()
        val minutes = ((_hours - hours) * 60).toInt()

        return when {
            hours > 0 -> "$hours hours and $minutes minutes"
            minutes > 0 -> "$minutes minutes"
            else -> "0 minutes"
        }
    }
}