package com.ilyasov.sci_king.domain.interactor.usecase.firebase

import com.ilyasov.sci_king.data.db.repository.FirebaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInUserUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
) {
    fun execute(email: String, password: String) =
        firebaseRepository.signInUser(email, password)
}