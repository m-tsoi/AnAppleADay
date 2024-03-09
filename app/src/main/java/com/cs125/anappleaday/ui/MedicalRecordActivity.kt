package com.cs125.anappleaday.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.record.models.user.MedicalRecord
import com.cs125.anappleaday.databinding.ActivityMedicalRecordsBinding
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class MedicalRecordActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMedicalRecordsBinding
    private lateinit var profileServices: FbProfileServices
    private lateinit var fbAuth: FBAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init firebase
        fbAuth = FBAuth()
        profileServices = FbProfileServices(Firebase.firestore)

        // Bind UI
        binding = ActivityMedicalRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<Button>(R.id.submitBtn).setOnClickListener{
            val allergies = binding.allergies.text.toString()
                .split("[,\\s]+".toRegex())
                .toMutableList()
            val foodRestrictions = binding.foodRestrictions.text.toString()
                .split("[,\\s]+".toRegex())
                .toMutableList()
            val disorders = binding.disorders.text.toString()
                .split("[,\\s]+".toRegex())
                .toMutableList()

            val userId = fbAuth.getUser()?.uid!!
            Log.d("ProfileId", userId)

            lifecycleScope.launch {
                val profileToUpdate = profileServices.getProfile(userId)
                if (profileToUpdate != null) {
                    profileToUpdate.medicalRecords = MedicalRecord(
                        allergies,
                        foodRestrictions,
                        disorders
                    )
                    profileServices.updateProfile(userId, profileToUpdate)
                        .addOnSuccessListener {
                            Toast.makeText(this@MedicalRecordActivity,
                                "Added medical records to profile.",
                                Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this@MedicalRecordActivity,
                                SelectPlanActivity::class.java))
                        }.addOnFailureListener{
                            Toast.makeText(this@MedicalRecordActivity,
                                "Failed to add medical records to profile.",
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                } else {
                    Toast.makeText(this@MedicalRecordActivity,
                        "Profile does not exist. Redirect to profile creation.",
                        Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@MedicalRecordActivity,
                        ProfileCreationActivity::class.java))
                }
            }
        }
    }
}