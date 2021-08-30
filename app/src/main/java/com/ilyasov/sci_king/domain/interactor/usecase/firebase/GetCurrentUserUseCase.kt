package com.ilyasov.sci_king.domain.interactor.usecase.firebase

import com.ilyasov.sci_king.data.db.repository.FirebaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentUserUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
) {
    fun execute() = firebaseRepository.getCurrentUser()
}