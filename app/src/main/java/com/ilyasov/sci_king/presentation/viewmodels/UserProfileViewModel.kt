package com.ilyasov.sci_king.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.ilyasov.sci_king.domain.interactor.usecase.firebase.GetCurrentUserUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.firebase.LogOutUseCase
import com.ilyasov.sci_king.util.Constants.Companion.DISPLAY_NAME_REQUIRED_MSG
import com.ilyasov.sci_king.util.Constants.Companion.PROFILE_PICS_DIR
import com.ilyasov.sci_king.util.Constants.Companion.PROFILE_UPDATED_MSG
import com.ilyasov.sci_king.util.Constants.Companion.START_LOADING
import com.ilyasov.sci_king.util.Constants.Companion.STOP_LOADING
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {
    private var profileImageUrl: String? = null
    private val currentUser: FirebaseUser by lazy { getCurrentUserUseCase.execute()!! }

    val errorStateLiveData: MutableLiveData<String> = MutableLiveData()
    val callbackLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val changeUserDisplayNameLiveData: MutableLiveData<String> = MutableLiveData()
    val emailVerificationLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val imageLoadingLiveData: MutableLiveData<Pair<RequestOptions, String>> = MutableLiveData()

    fun loadUserInformation() {
        if (currentUser.photoUrl != null) {
            profileImageUrl = currentUser.photoUrl.toString()
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
            imageLoadingLiveData.postValue(Pair(requestOptions, profileImageUrl!!))
        }
        currentUser.displayName.let { user_display_name ->
            changeUserDisplayNameLiveData.postValue(user_display_name)
        }
        if (currentUser.isEmailVerified) {
            emailVerificationLiveData.postValue(EMAIL_VERIFIED)
        } else {
            emailVerificationLiveData.postValue(EMAIL_NOT_VERIFIED)
        }
    }

    fun saveUserInformation(displayName: String) {
        if (displayName.isEmpty()) {
            errorStateLiveData.postValue(DISPLAY_NAME_REQUIRED_MSG)
            return
        }
        if (profileImageUrl != null) {
            val profile = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(Uri.parse(profileImageUrl))
                .build()
            currentUser.updateProfile(profile)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callbackLiveData.postValue(PROFILE_UPDATED_MSG)
                    }
                }
        }
    }

    fun uploadImageToFirebaseStorage(uriProfileImage: Uri) {
        val profileImageRef = FirebaseStorage.getInstance()
            .getReference(PROFILE_PICS_DIR + "/" + System.currentTimeMillis() + ".jpg")
        loadingMutableLiveData.postValue(START_LOADING)
        profileImageRef.putFile(uriProfileImage)
            .addOnSuccessListener { taskSnapshot ->
                loadingMutableLiveData.postValue(STOP_LOADING)
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { url ->
                    profileImageUrl = url.toString()
                }
            }
            .addOnFailureListener { e ->
                loadingMutableLiveData.postValue(STOP_LOADING)
                callbackLiveData.postValue(e.message)
            }
    }

    fun sendEmailVerification(onCompleteCallback: () -> Unit) {
        currentUser.sendEmailVerification().addOnCompleteListener {
            onCompleteCallback.invoke()
        }
    }

    fun logOut() = logOutUseCase.execute()

    companion object {
        const val EMAIL_VERIFIED = true
        const val EMAIL_NOT_VERIFIED = false
    }
}