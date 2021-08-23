package com.ilyasov.sci_king.data.db.repository

import com.ilyasov.sci_king.data.db.cache.UserSciArticlesDAO
import com.ilyasov.sci_king.domain.entity.SciArticle
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val userSciArticlesDAO: UserSciArticlesDAO
) : LocalRepository {
    override suspend fun addSciArticle(article: SciArticle) =
        userSciArticlesDAO.addSciArticle(article)

    override suspend fun removeSciArticle(article: SciArticle) =
        userSciArticlesDAO.removeSciArticle(article)

    override suspend fun checkArticleExistence(id: String): Boolean =
        userSciArticlesDAO.checkArticleExistence(id)

    override suspend fun getUserSavedArticles(): List<SciArticle> =
        userSciArticlesDAO.getUserSavedArticles()
}