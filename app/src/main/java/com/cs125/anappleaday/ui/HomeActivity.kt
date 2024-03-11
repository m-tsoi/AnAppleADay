package com.cs125.anappleaday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.cs125.anappleaday.R
import com.cs125.anappleaday.services.auth.FBAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var fbAuth: FBAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fbAuth = FBAuth()

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

        // Logout
        findViewById<Button>(R.id.logout_button)
            .setOnClickListener{
                fbAuth.logout()
                Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
    }

    override fun onStart() {
        super.onStart()
        if (!fbAuth.isCurrentUser()) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}