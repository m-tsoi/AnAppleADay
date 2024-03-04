package com.cs125.anappleaday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.cs125.anappleaday.R
import com.cs125.anappleaday.databinding.ActivityRegisterBinding
import com.cs125.anappleaday.services.auth.FBAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var fbAuth: FBAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fbAuth = FBAuth()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<Button>(R.id.register_button)
            .setOnClickListener{

                // Form fields
                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()
                val password2 = binding.editTextConfirmPassword.text.toString()

                if (validate(email, password, password2)) {
                    fbAuth.register(email, password).addOnCompleteListener(this){ task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this,
                                    "Account created successfully",
                                    Toast.LENGTH_SHORT)
                                .show()
                            val sendIntent = Intent(this,
                                ProfileCreationActivity::class.java)
                            startActivity(sendIntent)
                        } else {
                            Toast.makeText(this,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }
            }
    }

    private fun validate(email: String, password: String, password2: String): Boolean {
        if  (email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            Toast
                .makeText(this,
                    "Empty fields",
                    Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast
                .makeText(this,
                    "Invalid email",
                    Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (password.length < 8) {
            Toast
                .makeText(this,
                    "Password must have at least 8 character",
                    Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (password != password2) {
            Toast.makeText(this,
                "Password doest not match",
                Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }
}