package com.ilyasov.sci_king.domain.interactor.usecase.user_articles

import com.ilyasov.sci_king.data.db.repository.LocalRepository
import com.ilyasov.sci_king.domain.entity.SciArticle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSavedArticlesUseCase @Inject constructor(
    private val repo: LocalRepository
) {
    suspend fun execute(): MutableList<SciArticle>  = repo.getUserSavedArticles()
}