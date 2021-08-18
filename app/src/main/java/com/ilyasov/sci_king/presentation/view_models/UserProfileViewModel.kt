package com.ilyasov.sci_king.presentation.view_models

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage

class UserProfileViewModel : ViewModel() {
    private var profileImageUrl: String? = null
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val errorStateLiveData: MutableLiveData<String> = MutableLiveData()
    val callbackLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val changeUserDisplayNameLiveData: MutableLiveData<String> = MutableLiveData()
    val emailVerificationLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val imageLoadingLiveData: MutableLiveData<RequestOptions> = MutableLiveData()

    fun loadUserInformation() {
        val user = mAuth.currentUser
        if (user != null) {
            if (user.photoUrl != null) {
                val requestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                    .skipMemoryCache(true)
                imageLoadingLiveData.postValue(requestOptions)
            }
            user.displayName.let { user_display_name ->
                changeUserDisplayNameLiveData.postValue(user_display_name)
            }
            if (user.isEmailVerified) {
                emailVerificationLiveData.postValue(true)
            } else {
                emailVerificationLiveData.postValue(false)
            }
        } //TODO add check error when user == null
    }

    fun saveUserInformation(displayName: String) {
        if (displayName.isEmpty()) {
            errorStateLiveData.postValue("Name required")
            return
        }
        val user = mAuth.currentUser
        if (user != null && profileImageUrl != null) {
            val profile = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(Uri.parse(profileImageUrl))
                .build()
            user.updateProfile(profile)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callbackLiveData.postValue("Profile Updated")
                    }
                }
        }
    }

    fun uploadImageToFirebaseStorage(uriProfileImage: Uri?) {
        val profileImageRef = FirebaseStorage.getInstance()
            .getReference("profile_pics/" + System.currentTimeMillis() + ".jpg")
        if (uriProfileImage != null) {
            loadingMutableLiveData.postValue(true)
            profileImageRef.putFile(uriProfileImage)
                .addOnSuccessListener { taskSnapshot ->
                    loadingMutableLiveData.postValue(false)
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { url ->
                        profileImageUrl = url.toString()
                    }
                }
                .addOnFailureListener { e ->
                    loadingMutableLiveData.postValue(false)
                    callbackLiveData.postValue(e.message)
                }
        }
    }

}