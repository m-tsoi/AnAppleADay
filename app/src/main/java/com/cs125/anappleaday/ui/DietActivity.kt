package com.cs125.anappleaday.ui

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.R
import com.cs125.anappleaday.api.ApiMain
import com.google.gson.JsonObject

import retrofit2.Callback

import android.health.connect.datatypes.NutritionRecord


class DietActivity : AppCompatActivity() {

    // will add number changing functionality based off API value??? (or from database)
    // will add in adding food eaten that day

    private lateinit var textScore : TextView
    private lateinit var recyclerRecommendations : RecyclerView
    private lateinit var buttonEnter : Button
    private lateinit var buttonView : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet)

        //textDiet = findViewById(R.id.textDiet)
        textScore = findViewById(R.id.textScore)
        recyclerRecommendations = findViewById<RecyclerView>(R.id.recyclerRecommendations)
        buttonEnter = findViewById<Button>(R.id.buttonEnter)
        buttonView = findViewById<Button>(R.id.buttonView)

        // database?
        // get information on previous day's score
        // may also need this to display meals and stuff after entered
        val db = Firebase.firestore




    }
    // opens search and view respectively
    fun openDietSearch(view: View){
        val intent = Intent(this, DietSearchActivity::class.java)
        startActivity(intent)
    }

    fun openDietView(view: View){
        val intent = Intent(this, DietViewActivity::class.java)
        startActivity(intent)
    }

    private fun updateUI(response: JsonObject) {
        // Update UI based on the API response
        // probably for the uhhhhhhh firebase score and idk suggestions???
    }
}