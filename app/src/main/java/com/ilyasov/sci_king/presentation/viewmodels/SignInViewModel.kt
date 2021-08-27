package com.ilyasov.sci_king.presentation.viewmodels

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.domain.entity.CustomError
import com.ilyasov.sci_king.domain.interactor.usecase.firebase.SignInUserUseCase
import com.ilyasov.sci_king.util.Constants.Companion.EMAIL_CHECK_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.EMAIL_REQUIRED_MSG
import com.ilyasov.sci_king.util.Constants.Companion.EMPTY_PASSWORD_MSG
import com.ilyasov.sci_king.util.Constants.Companion.INVALID_EMAIL_MSG
import com.ilyasov.sci_king.util.Constants.Companion.PASSWORD_CHECK_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.PASSWORD_LENGTH_ERR_MSG
import com.ilyasov.sci_king.util.Constants.Companion.SERVER_SIGN_IN_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.UNEXPECTED_SIGN_IN_ERROR_MSG
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val signInUserUseCase: SignInUserUseCase,
) : ViewModel() {
    val errorStateLiveData: MutableLiveData<CustomError> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val navigationStateLiveData: MutableLiveData</*navHostFragment id*/ Int> = MutableLiveData()

    fun userLogin(email: String, password: String) {
        if (noErrors(email, password)) {
            loadingMutableLiveData.postValue(true)
            signInUserUseCase.execute(email, password)
                .addOnCompleteListener { task -> onCompleteLoginTask(task) }
        }
    }

    private fun onCompleteLoginTask(task: Task<AuthResult>) {
        loadingMutableLiveData.postValue(false)
        if (task.isSuccessful) {
            navigationStateLiveData.postValue(R.id.navHostFragmentActivityRoot)
        } else {
            errorStateLiveData.postValue(
                CustomError(
                    SERVER_SIGN_IN_ERROR,
                    task.exception!!.message ?: UNEXPECTED_SIGN_IN_ERROR_MSG
                )
            )
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