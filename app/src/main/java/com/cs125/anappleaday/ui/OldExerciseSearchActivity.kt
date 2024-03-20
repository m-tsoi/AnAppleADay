package com.cs125.anappleaday.ui

import android.os.Bundle
import android.util.Log
import com.cs125.anappleaday.R
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.api.ApiMain
import com.cs125.anappleaday.data.enumTypes.ExerciseData
import com.cs125.anappleaday.data.record.models.live.ActivityData
import android.health.connect.datatypes.ExerciseSessionRecord
import com.cs125.anappleaday.data.recycler.ExerciseResultAdapter
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbActivityServices
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OldExerciseSearchActivity : AppCompatActivity() {

    // firebase
    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var personicleServices: FbPersonicleServices
    private lateinit var exerciseServices : FbActivityServices

    // UI components
    private lateinit var searchView : SearchView
    private lateinit var recyclerResults : RecyclerView
    private lateinit var exerciseResultAdapter: ExerciseResultAdapter

    // Exercise
    private val apiService = ApiMain.getNinjaServices()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.old_activity_exercise_search)

        // fb setting
        fbAuth = FBAuth()
        profileServices = FbProfileServices(com.google.firebase.ktx.Firebase.firestore)
        personicleServices = FbPersonicleServices(com.google.firebase.ktx.Firebase.firestore)
        exerciseServices = FbActivityServices(com.google.firebase.ktx.Firebase.firestore)

        // setting UI components
        searchView = findViewById<SearchView>(R.id.searchView)
        recyclerResults = findViewById<RecyclerView>(R.id.recyclerResults)

        // listener for search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { fetchSearchResults(it) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    private fun fetchSearchResults(query: String) {
        val activity = query
        val weight = "medium" // Example weight
        val duration = "45" // Example duration

        val call = apiService.getCaloriesBurnedExercises(activity, weight, duration)
        call.enqueue(object : Callback<List<ExerciseSessionRecord>> {
            override fun onResponse(call: Call<List<ExerciseData>>, response: Response<List<ExerciseSessionRecord>>) {
                if (response.isSuccessful) {
                    val exerciseDataList = response.body()
                    val mutableDataList: MutableList<ExerciseSessionRecord> = exerciseDataList?.toMutableList() ?: mutableListOf()

                    val onAddClickListener: (Int) -> Unit = { position ->
                        var selectedExerciseData = mutableDataList[position]

                        Log.d("ExerciseSearchActivity", "Selected ExerciseData: $selectedExerciseData")
                        addExerciseDataToFirebase(selectedExerciseData)
                    }

                    exerciseResultAdapter = ExerciseResultAdapter(mutableDataList,onAddClickListener)
                    recyclerResults.adapter = exerciseResultAdapter

                } else {
                    Log.e("ExerciseSearchActivity", "API Request failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ExerciseData>>, t: Throwable) {
                Log.e("ExerciseSearchActivity", "API Request failed", t)
            }
        })
    }

    private fun addExerciseDataToFirebase(nutritionData: ActivityData) {
        // do firebase stuff here (
        // add to nutrition, which is Date -> <MutableList>NutritionData

        val userId =  fbAuth.getUser()?.uid
        if (  userId != null) {
            lifecycleScope.launch {
                val profile = profileServices.getProfile(userId)
                val personicle = personicleServices.getPersonicle(profile?.personicleId!!)
                if (personicle != null) {
                    if (personicle.dietDataId!= null)
                    // if dietDataId is there this SHOULD work....... I hope\
                    // sorry idk how to reinitialize the personicle
                        exerciseServices.addExerciseSessionRecordRecord(personicle.dietDataId!!, nutritionData)
                }
            }
        }
    }
}