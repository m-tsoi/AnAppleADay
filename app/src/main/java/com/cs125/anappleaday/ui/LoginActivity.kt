package com.cs125.anappleaday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.cs125.anappleaday.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Login button functionalities
        findViewById<Button>(R.id.login_button)
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                //TODO: send login info to backend

                finish()
                val sendIntent = Intent(this, HomeActivity::class.java)
                startActivity(sendIntent)
            }

        // Register button functionalities
        findViewById<Button>(R.id.register_button)
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                finish()
                val sendIntent = Intent(this, RegisterActivity::class.java)
                startActivity(sendIntent)
            }
    }
}