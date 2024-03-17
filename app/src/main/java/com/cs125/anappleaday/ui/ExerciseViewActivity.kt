package com.cs125.anappleaday.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.enumTypes.ExerciseData
import com.cs125.anappleaday.data.record.models.live.ActivityData
import com.cs125.anappleaday.data.recycler.ExerciseViewAdapter
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbActivityServices
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

import java.util.Date


class ExerciseViewActivity : AppCompatActivity() { // displays meals corresponding to current Date

    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var personicleServices: FbPersonicleServices
    private lateinit var exerciseServices: FbActivityServices
    private lateinit var recyclerExercises: RecyclerView
    private lateinit var exerciseViewAdapter: ExerciseViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_view)

        recyclerExercises = findViewById<RecyclerView>(R.id.recyclerExercises)

        // initialize firebase
        fbAuth = FBAuth()
        profileServices = FbProfileServices(Firebase.firestore)
        personicleServices = FbPersonicleServices(Firebase.firestore)
        exerciseServices = FbActivityServices(Firebase.firestore)
    }

    override fun onStart(){
        var exercisesDataList = mutableListOf<ExerciseData>()

        val userId =  fbAuth.getUser()?.uid
        if (  userId != null) {
            lifecycleScope.launch {
                val profile = profileServices.getProfile(userId)
                val personicle = personicleServices.getPersonicle(profile?.personicleId!!)
                if (personicle != null) {
                    if (personicle.dietDataId != null){
                        val exerciseData = exerciseServices.getActivityData(personicle?.activityDataId!!)
                        if (exerciseData != null) {
                            exercisesDataList = exerciseData.nutrition[Date()]!!
                        }

                        val onDeleteClickListener: (MutableList<ExerciseData>) -> Unit = { dataSet ->
                            Log.d("DietViewActivity", "Reset NutritionData: $dataSet")
                            launch {
                                exerciseServices.resetNutritionData(
                                    personicle.dietDataId,
                                    Date(),
                                    dataSet
                                )
                            }
                        }

                        exerciseViewAdapter = ExerciseViewAdapter(exercisesDataList,onDeleteClickListener )
                        recyclerExercises.adapter = exerciseViewAdapter


                    }
                }
            }
        }


        super.onStart()

    }


}