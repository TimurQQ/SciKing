package com.ilyasov.sci_king.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.ilyasov.sci_king.domain.interactor.usecase.firebase.GetCurrentUserUseCase
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {
    fun getCurrentUser(): FirebaseUser? = getCurrentUserUseCase.execute()
}