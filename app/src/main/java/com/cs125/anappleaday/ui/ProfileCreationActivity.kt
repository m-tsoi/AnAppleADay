package com.cs125.anappleaday.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cs125.anappleaday.R
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class ProfileCreationActivity: AppCompatActivity() {
    private lateinit var genderAutoCompleteText: MaterialAutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_creattion)

        genderAutoCompleteText = findViewById(R.id.selectTextGender)


        findViewById<Button>(R.id.register_button)
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                //TODO: send profile info to backend

                finish()
                val sendIntent = Intent(this, SelectPlanActivity::class.java)
                startActivity(sendIntent)
            }
    }
}