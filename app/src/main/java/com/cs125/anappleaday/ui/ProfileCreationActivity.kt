package com.cs125.anappleaday.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cs125.anappleaday.R
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class ProfileCreationActivity: AppCompatActivity() {
    private lateinit var genderAutoCompleteText: MaterialAutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_creattion)

        genderAutoCompleteText = findViewById(R.id.selectTextGender)

        val sendIntent = Intent(this, SelectPlanActivity::class.java)
        startActivity(sendIntent)
    }
}