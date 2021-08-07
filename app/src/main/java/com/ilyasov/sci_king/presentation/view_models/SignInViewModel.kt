package com.ilyasov.sci_king.presentation.view_models

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.util.Constants.Companion.EMAIL_CHECK_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.PASSWORD_CHECK_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.SERVER_SIGN_IN_ERROR

class SignInViewModel : ViewModel() {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val errorStateLiveData: MutableLiveData<Pair<String, String>> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val navigationStateLiveData: MutableLiveData</*navHostFragment id*/ Int> = MutableLiveData()

    fun userLogin(email: String, password: String) {
        if (email.isEmpty()) {
            errorStateLiveData.postValue(Pair(EMAIL_CHECK_ERROR, "Email is required"))
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorStateLiveData.postValue(Pair(EMAIL_CHECK_ERROR, "Please enter a valid email"))
        } else if (password.isEmpty()) {
            errorStateLiveData.postValue(Pair(PASSWORD_CHECK_ERROR, "Password is required"))
        } else if (password.length < 6) {
            errorStateLiveData.postValue(
                Pair(PASSWORD_CHECK_ERROR, "Minimum length of password should be 6")
            )
        } else {
            loadingMutableLiveData.postValue(true)
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    loadingMutableLiveData.postValue(false)
                    if (task.isSuccessful) {
                        navigationStateLiveData.postValue(R.id.activity_root__fragment__nav_host)
                    } else {
                        errorStateLiveData.postValue(
                            Pair(
                                SERVER_SIGN_IN_ERROR,
                                task.exception!!.message ?: "Unexpected Sign In Error"
                            )
                        )
                    }
                }
        }
    }
}