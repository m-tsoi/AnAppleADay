package com.cs125.anappleaday.services.user

import com.cs125.anappleaday.data.enumTypes.Gender
import com.cs125.anappleaday.data.sql.dto.ProfileDto
import com.cs125.anappleaday.data.sql.models.user.Profile
import com.cs125.anappleaday.data.sql.repository.ProfileRepository
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class ProfileServices (private val profileRepo: ProfileRepository, firestore: FirebaseFirestore) {
    private val fbProfileServices = FbProfileServices(firestore)

    suspend fun onCreateProfile(_profile: ProfileDto): Result<Profile> {
        val newLocalProfile = Profile(
            pid = UUID.randomUUID(),
            name = _profile.name,
            avatar = _profile.avatar,
            age = _profile.age,
            gender = Gender.valueOf(_profile.gender),
            personicleId = "",
            medicalRecordId = "",
        )

        profileRepo.insert(newLocalProfile).onSuccess {
            return if (fbProfileServices.addProfile(newLocalProfile).isSuccessful) {
                Result.success(newLocalProfile)
            } else {
                // Rollback profile created in SQL
                profileRepo.delete(newLocalProfile.pid.toString())
                Result.failure(Exception("Failed to project profile in Firestore"))
            }
        }

        return Result.failure(Exception("Failed to create profile"))

    }

    suspend fun onUpdateProfile(_profile: Profile): Result<Profile> {
        val currentProfile = profileRepo.get(_profile.pid.toString()).getOrNull()
        profileRepo.update(_profile).onSuccess {
            return if (fbProfileServices.updateProfile(_profile).isSuccessful) {
                Result.success(_profile)
            } else {
                if (currentProfile != null) {
                    profileRepo.update(currentProfile)
                }
                Result.failure(Exception("Failed to project profile in Firestore"))
            }
        }

        return Result.failure(Exception("Failed to update profile"))
    }

    suspend fun onDeleteProfile(pid: String) {
        profileRepo.delete(pid)
        fbProfileServices.delete(pid)
    }
}