package com.cs125.anappleaday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.cs125.anappleaday.R

class SelectPlanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_plan)

        // Confirm button functionalities
        findViewById<Button>(R.id.confirm_button)
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                //TODO: send health plan info to backend

                finish()
                val sendIntent = Intent(this, HomeActivity::class.java)
                startActivity(sendIntent)
            }
    }
}