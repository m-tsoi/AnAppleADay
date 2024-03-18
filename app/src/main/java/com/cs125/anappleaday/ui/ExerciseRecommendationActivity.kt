import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.record.models.user.Profile

class ExerciseRecommendationActivity : AppCompatActivity() {
    private val exerciseTypes = listOf("Lifting", "Cardio", "Stretching")
    private lateinit var exerciseTypeEditText: EditText
    private lateinit var durationEditText: EditText
    private lateinit var submitBtn: Button
    private lateinit var addedExerciseTextView: TextView
    private lateinit var userProfile: Profile // Assuming userProfile is obtained from somewhere

    // Target calories for tomorrow
    private val targetCaloriesTomorrow = 2000 // Example target calories

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        exerciseTypeEditText = findViewById(R.id.exerciseTypeEditText)
        durationEditText = findViewById(R.id.durationEditText)
        submitBtn = findViewById(R.id.submitBtn)
        addedExerciseTextView = findViewById(R.id.addedExerciseTextView)

        submitBtn.setOnClickListener {
            val exerciseType = exerciseTypeEditText.text.toString()
            val duration = durationEditText.text.toString().toIntOrNull()

            if (exerciseTypes.contains(exerciseType) && duration != null && duration > 0) {
                val displayText = "Exercise Type: $exerciseType, Duration: $duration minutes"

                val weight = userProfile.weight.toDouble() // Get user weight
                val met = calculateMET(weight, exerciseType)
                val caloriesBurned = calculateCaloriesBurned(weight, duration.toDouble(), exerciseType)
                val differenceCalories = caloriesBurned - targetCaloriesTomorrow

                // Display
                val resultText = "$displayText\nCalories Burned: $caloriesBurned kcal\n\n"
                addedExerciseTextView.text = resultText

                if (differenceCalories > 0) {
                    addedExerciseTextView.append("You've exceeded your target calories for tomorrow. Consider reducing intake or increasing exercise.")
                } else {
                    val recommendedExerciseMinutes = (differenceCalories / met).toInt()
                    addedExerciseTextView.append("Recommended exercise for tomorrow: $recommendedExerciseMinutes minutes of $exerciseType")
                }
            } else {
                showToast("Please enter a valid exercise type (Lifting, Cardio, Stretching) and duration")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@ExerciseRecommendationActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun calculateMET(weight: Double, activityType: String): Double {
        // Assume: standard resting metabolic rate of 1.0 kcal/kg/hour
        val rmr = 1.0

        val energyExpenditureLifting = 3.0
        val energyExpenditureCardio = 7.0
        val energyExpenditureStretching = 2.0

        // MET by activity
        return when (activityType.toLowerCase()) {
            "lifting" -> energyExpenditureLifting / (weight * rmr)
            "cardio" -> energyExpenditureCardio / (weight * rmr)
            "stretching" -> energyExpenditureStretching / (weight * rmr)
            else -> 0.0
        }
    }

    private fun calculateCaloriesBurned(weight: Double, duration: Double, activityType: String): Double {
        val met = calculateMET(weight, activityType)
        return met * weight * duration / 60 // Duration should be in hours
    }
}
