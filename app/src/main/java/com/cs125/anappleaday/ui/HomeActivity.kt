package com.cs125.anappleaday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.cs125.anappleaday.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Sleep button functionalities
        findViewById<TextView>(R.id.sleep_score)
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")

                val sendIntent = Intent(this, SleepActivity::class.java)
                startActivity(sendIntent)
            }

        // Personicle button functionalities
        findViewById<Button>(R.id.personicle_button)
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")

                val sendIntent = Intent(this, PersonicleActivity::class.java)
                startActivity(sendIntent)
            }

        // Change plan button functionalities
        findViewById<Button>(R.id.change_plan_button)
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")

                val sendIntent = Intent(this, SelectPlanActivity::class.java)
                startActivity(sendIntent)
            }
    }
}