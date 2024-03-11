package com.cs125.anappleaday.services.wellness

import android.content.Context
import android.widget.Toast
import com.cs125.anappleaday.data.enumTypes.ActivityLevel
import com.cs125.anappleaday.data.enumTypes.HealthGoal
import com.cs125.anappleaday.data.record.models.healthPlans.DietPlan
import com.cs125.anappleaday.data.record.models.healthPlans.ExercisePlan
import com.cs125.anappleaday.data.record.models.healthPlans.HealthPlan
import com.cs125.anappleaday.data.record.models.healthPlans.SleepPlan
import com.cs125.anappleaday.services.firestore.FbHealthPlanServices
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class InitHealthDataServices (firestore: FirebaseFirestore) {
    private val fbHealthPlanServices: FbHealthPlanServices = FbHealthPlanServices(firestore)

    fun initData(context: Context,
                         healthGoal: HealthGoal,
                         rmr: Double,
                         startDate: Long,
                         endDate: Long): Task<DocumentReference> {
        val healthPlan = initHealthData(healthGoal, rmr, startDate, endDate)
        return fbHealthPlanServices.createHealthPlan(healthPlan)
            .addOnSuccessListener {
                Toast.makeText(context, "Personicle was created.",
                    Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context, "Failed to create Health Plan",
                    Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private val generalSleepPlan = SleepPlan(
                sleepDuration = 8.0,
                wakeupTime = 7,
            )

        fun initHealthData(healthGoal: HealthGoal, rmr: Double,
                           startDate: Long,
                           endDate: Long): HealthPlan  {
            val plans =  when (healthGoal) {
                HealthGoal.BE_HEALTHY -> startHealthyPlan(rmr)
                HealthGoal.BULK_UP -> startBulkPlan(rmr)
                HealthGoal.STAY_LEAN -> startLeanPlan(rmr)
                HealthGoal.LOSE_FAT -> startLoseFatPlan(rmr)
            }

            return HealthPlan(
                healthGoal = healthGoal,
                score = 0.0,
                dietPlan = plans["dietPlan"] as DietPlan,
                exercisePlan = plans["exercisePlan"] as ExercisePlan,
                sleepPlan = generalSleepPlan,
                startDate = startDate,
                endDate = endDate
            )
        }

        private fun startHealthyPlan(rmr: Double): HashMap<String, Any> {
            // macronutrients src:"https://www.ncbi.nlm.nih.gov/pmc/articles/PMC1479724/"
            // plan: src:"https://www.ncbi.nlm.nih.gov/pmc/articles/PMC5793336/"
            val exercisePlan = ExercisePlan(
                exerciseType = ActivityLevel.MODERATE,
                dailyDuration = 0.3
            )
            val dietPlan = DietPlan(
                dayCaloriesIntake = exercisePlan.exerciseType.value * rmr,    // TODO: compute gain/loss calories per day
                dayProteinIntake = 0.25,
                dayCarbIntake = 0.55,
                dayFatIntake = 0.25,
                supportedNutrients = mutableListOf(
                    "fish oil", "multi-vitamin", "calcium", "vitamin D", "vitamin E"
                ),
                limitedFoods = mutableListOf(
                    "red meat", "saturated fats", "refined grains", "potatoes",
                    "soft drinks", "sweets"
                )
            )
            return hashMapOf(
                "dietPlan" to dietPlan,
                "exercisePlan" to exercisePlan
            )
        }

        private fun startBulkPlan(rmr: Double): HashMap<String, Any> {
            val exercisePlan = ExercisePlan(
                exerciseType = ActivityLevel.VERY_ACTIVE,
                suggestedExercises = mutableListOf("weight trainings"), // TODO: replace by ninja api
                dailyDuration = 1.0
            )
            val dietPlan = DietPlan(
                dayCaloriesIntake = exercisePlan.exerciseType.value * rmr * 1.2,
                dayProteinIntake = 0.35,
                dayCarbIntake = 0.50,
                dayFatIntake = 0.15,
                supportedNutrients = mutableListOf(
                    "Water", "Calcium", "Magnesium", "Potassium", "Iron",
                    "B12", "Vitamin D", "Beta-Alanine", "Glutamine"
                ) // src:"https://dailyburn.com/life/health/top-nutrients-build-muscle/"
            )
            return hashMapOf(
                "dietPlan" to dietPlan,
                "exercisePlan" to exercisePlan
            )
        }

        private fun startLeanPlan(rmr: Double): HashMap<String, Any> {
            // Weight Loss Plan
            // Use Fast Metabolism Diet
            // src: "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC5793336/"
            // Some limited foods are omitted since it is not ideal for students
            val exercisePlan = ExercisePlan(
                exerciseType = ActivityLevel.VERY_ACTIVE,
                dailyDuration = 0.45,
            )
            val dietPlan = DietPlan(
                dayCaloriesIntake = exercisePlan.exerciseType.value * rmr * 0.9,
                dayProteinIntake = 0.30,
                dayCarbIntake = 0.45,
                dayFatIntake = 0.25,
                supportedNutrients = mutableListOf(
                    "multi-vitamin", "omega-3", "omega-6"
                ),
                limitedFoods = mutableListOf(
                    "wheat", "corn", "dairy", "soy", "dried fruit", "artificial sweeteners",
                )
            )
            return hashMapOf(
                "dietPlan" to dietPlan,
                "exercisePlan" to exercisePlan
            )
        }

        private fun startLoseFatPlan(rmr: Double): HashMap<String, Any> {
            // Weight Loss Plan
            // Use Aggressive Weight Loss (but not strictly)
            // src: "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC5793336/"
            val exercisePlan = ExercisePlan(
                exerciseType = ActivityLevel.EXTRA_ACTIVE,
                dailyDuration = 1.5
            )
            val dietPlan = DietPlan(
                dayCaloriesIntake = exercisePlan.exerciseType.value * rmr * 0.8,
                dayProteinIntake = 0.3,
                dayCarbIntake = 0.5,
                dayFatIntake = 0.2,
                supportedNutrients = mutableListOf(
                    "omega-3", "omega-6", "B12", "vintamin D", "zinc", "iodine"
                ),
                limitedFoods = mutableListOf(
                    "dairy",  "low-fiber", "fast food", "sweets"
                )
            )
            return hashMapOf(
                "dietPlan" to dietPlan,
                "exercisePlan" to exercisePlan
            )
        }
    }
}