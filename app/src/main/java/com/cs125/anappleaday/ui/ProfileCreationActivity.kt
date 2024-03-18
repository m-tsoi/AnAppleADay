package com.cs125.anappleaday.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.enumTypes.Gender
import com.cs125.anappleaday.data.record.models.user.MedicalRecord
import com.cs125.anappleaday.data.record.models.user.Profile
import com.cs125.anappleaday.databinding.ActivityProfileCreattionBinding
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.cs125.anappleaday.utils.ImageHelper
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ProfileCreationActivity: AppCompatActivity() {
    private lateinit var binding: ActivityProfileCreattionBinding
    private lateinit var profileServices: FbProfileServices
    private lateinit var fbAuth: FBAuth
    private lateinit var storageRef: StorageReference

    private var avatarUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_creattion)

        // Init Firebase
        profileServices = FbProfileServices(Firebase.firestore)
        fbAuth = FBAuth()
        storageRef = Firebase.storage.reference

        // Binding UI
        binding = ActivityProfileCreattionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Preload default avatar
        val avatarView = findViewById<ImageView>(R.id.avatarView)
        val initial = fbAuth.getUser()?.email?.first()

        Glide
            .with(this)
            .asBitmap()
            .load("https://firebasestorage.googleapis.com/v0/b/anappleaday-bf6c3.appspot.com/o/avatars%2Fdefault_avatar.png?alt=media&token=a7f51466-d4ed-4151-a4ed-382d74a6d22f")
            //.load("https://ui-avatars.com/api/?name=${initial}&background=random")  <- the api suddenly failed
            .into(object: CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    avatarView.setImageBitmap(resource)
                    CoroutineScope(Dispatchers.Main).launch {
                        avatarUri = ImageHelper.bitmapToUri(resource, externalCacheDir)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    avatarView.setImageDrawable(placeholder)
                }
            })

        // Update avatar
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                uri ->
            if (uri != null) {
                avatarUri = uri
                avatarView.setImageURI(avatarUri)
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        avatarView.setOnClickListener{
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        // On submit listen
        findViewById<Button>(R.id.submitBtn).setOnClickListener{
            if (fbAuth.isCurrentUser()) {
                val name = binding.editTextName.text.toString()
                val age = binding.editNumberAge.text.toString().toInt()
                val gender = binding.selectGender.text.toString()
                val inches = binding.editHeightInch.text.toString().toInt()
                val feet = binding.editHeightFeet.text.toString().toInt()
                val height = feet * 12 + inches
                val weight = binding.editWeight.text.toString().toInt()

                ImageHelper.uploadImage( this, "avatars", storageRef, avatarUri!!)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            avatarUri = task.result
                        }
                    }.addOnFailureListener{
                    Toast.makeText(this,"Failed to upload avatar.", Toast.LENGTH_SHORT)
                        .show()
                    }
                fbAuth.updateUser(name, avatarUri.toString())

                // Create profile
                val profile = Profile(
                    age = age,
                    gender = Gender.valueOf(gender),
                    height = height,
                    weight = weight,
                    personicleId = UUID.randomUUID().toString(),
                    healthPlanId = null,
                    MedicalRecord(ArrayList(), ArrayList(), ArrayList())
                )
                val userId = fbAuth.getUser()?.uid
                if (userId != null) {
                    profileServices.createProfile(userId, profile)
                        .addOnCompleteListener{
                            task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Created profile successfully.",
                                    Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(this, MedicalRecordActivity::class.java))
                            }
                        }.addOnFailureListener{
                            Toast.makeText(this, "Failed to create profile.",
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            } else {
                Toast.makeText(this, "Unauthenticated user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "avatar.png")
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
    }

    fun drawableToUri(context: Context, drawable: Drawable): Uri {
        val bitmap = drawableToBitmap(drawable)
        return bitmapToUri(context, bitmap)
    }
}