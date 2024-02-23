package com.cs125.anappleaday.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.cs125.anappleaday.R
import com.cs125.anappleaday.databinding.ActivityLoginBinding
import com.cs125.anappleaday.services.auth.FBAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var fbAuth: FBAuth
    private lateinit var binding: ActivityLoginBinding

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        fbAuth = FBAuth()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Login button functionalities
        findViewById<Button>(R.id.login_button)
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")


                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    fbAuth.login(email, password).addOnCompleteListener{
                        if (it.isSuccessful) {
                            val sendIntent = Intent(this, HomeActivity::class.java)
                            startActivity(sendIntent)
                        } else {
                            Toast
                                .makeText(this, "Incorrect email or password.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast
                        .makeText(this, "Field cannot be blank", Toast.LENGTH_SHORT)
                        .show()

                }
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

//    override fun onStart() {
//        super.onStart()
//
//        if(fbAuth.isCurrentUser()){
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//        }
//    }
}