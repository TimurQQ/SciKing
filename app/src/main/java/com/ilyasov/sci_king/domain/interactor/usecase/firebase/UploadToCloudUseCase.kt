package com.ilyasov.sci_king.domain.interactor.usecase.firebase

import com.ilyasov.sci_king.data.db.repository.FirebaseRepository
import com.ilyasov.sci_king.domain.entity.SciArticle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadToCloudUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    fun execute(sciArticle: SciArticle) =
        firebaseRepository.uploadToCloud(sciArticle)
}