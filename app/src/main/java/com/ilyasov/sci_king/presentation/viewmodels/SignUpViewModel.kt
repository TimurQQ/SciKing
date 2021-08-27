package com.ilyasov.sci_king.presentation.viewmodels

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.domain.entity.CustomError
import com.ilyasov.sci_king.domain.interactor.usecase.firebase.SignUpUserUseCase
import com.ilyasov.sci_king.util.Constants.Companion.ALREADY_REGISTERED_MSG
import com.ilyasov.sci_king.util.Constants.Companion.EMAIL_CHECK_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.EMAIL_REQUIRED_MSG
import com.ilyasov.sci_king.util.Constants.Companion.EMPTY_PASSWORD_MSG
import com.ilyasov.sci_king.util.Constants.Companion.INVALID_EMAIL_MSG
import com.ilyasov.sci_king.util.Constants.Companion.PASSWORD_CHECK_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.PASSWORD_LENGTH_ERR_MSG
import com.ilyasov.sci_king.util.Constants.Companion.SERVER_SIGN_UP_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.UNEXPECTED_SIGN_UP_ERROR_MSG
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val signUpUserUseCase: SignUpUserUseCase,
) : ViewModel() {
    val errorStateLiveData: MutableLiveData<CustomError> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val navigationStateLiveData: MutableLiveData</*navHostFragment id*/ Int> = MutableLiveData()

    fun registerUser(email: String, password: String) {
        if (noErrors(email, password)) {
            loadingMutableLiveData.postValue(true)
            signUpUserUseCase.execute(email, password)
                .addOnCompleteListener { task -> onCompleteRegisterTask(task) }
        }
    }

    private fun onCompleteRegisterTask(task: Task<AuthResult>) {
        loadingMutableLiveData.postValue(false)
        if (task.isSuccessful) {
            navigationStateLiveData.postValue(R.id.navHostFragmentActivityRoot)
        } else {
            if (task.exception is FirebaseAuthUserCollisionException) {
                errorStateLiveData.postValue(
                    CustomError(SERVER_SIGN_UP_ERROR, ALREADY_REGISTERED_MSG)
                )
            } else {
                errorStateLiveData.postValue(
                    CustomError(
                        SERVER_SIGN_UP_ERROR,
                        task.exception!!.message ?: UNEXPECTED_SIGN_UP_ERROR_MSG
                    )
                )
            }
        }
    }

    private fun noErrors(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            errorStateLiveData.postValue(CustomError(EMAIL_CHECK_ERROR, EMAIL_REQUIRED_MSG))
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorStateLiveData.postValue(CustomError(EMAIL_CHECK_ERROR, INVALID_EMAIL_MSG))
        } else if (password.isEmpty()) {
            errorStateLiveData.postValue(CustomError(PASSWORD_CHECK_ERROR, EMPTY_PASSWORD_MSG))
        } else if (password.length < 6) {
            errorStateLiveData.postValue(CustomError(PASSWORD_CHECK_ERROR, PASSWORD_LENGTH_ERR_MSG))
        } else {
            return true
        }
        return false
    }
}