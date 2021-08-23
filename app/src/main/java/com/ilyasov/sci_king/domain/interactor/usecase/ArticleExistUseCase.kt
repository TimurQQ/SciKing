package com.ilyasov.sci_king.domain.interactor.usecase

import com.ilyasov.sci_king.data.db.repository.LocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleExistUseCase @Inject constructor(
    private val repo: LocalRepository
) {
    suspend fun execute(id: String) = repo.checkArticleExistence(id)
}