package com.cs125.anappleaday.services.profile

import com.cs125.anappleaday.data.enumTypes.Gender
import com.cs125.anappleaday.data.sql.dao.ProfileDao
import com.cs125.anappleaday.data.sql.dto.ProfileDto
import com.cs125.anappleaday.data.sql.models.user.Profile
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class ProfileJobServices (private val dao: ProfileDao, firestore: FirebaseFirestore) {
    private val fbProfileServices = FbProfileServices(firestore)

    suspend fun onCreateProfile(_profile: ProfileDto) {
        val newLocalProfile = Profile(
            pid = UUID.randomUUID(),
            name = _profile.name,
            avatar = _profile.avatar,
            age = _profile.age,
            gender = Gender.valueOf(_profile.gender),
            personicleId = "",
            medicalRecordId = "",
        )

        dao.insertProfile(newLocalProfile)
        fbProfileServices.addProfile(newLocalProfile)
    }

    suspend fun onUpdateProfile(_profile: Profile) {

    }

    suspend fun onDeleteProfile(pid: String) {

    }
}