package com.cs125.anappleaday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.cs125.anappleaday.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Register button functionalities
        findViewById<Button>(R.id.register_button)
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                //TODO: send register info to backend

                finish()
                val sendIntent = Intent(this, SelectPlanActivity::class.java)
                startActivity(sendIntent)
            }
    }
}