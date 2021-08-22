package com.ilyasov.sci_king.data.db.repository

import com.ilyasov.sci_king.data.db.cache.UserSciArticlesDAO
import com.ilyasov.sci_king.domain.entity.SciArticle
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val userSciArticlesDAO: UserSciArticlesDAO
) : LocalRepository {
    override fun addSciArticle(article: SciArticle) = userSciArticlesDAO.addSciArticle(article)

    override fun removeSciArticle(article: SciArticle) =
        userSciArticlesDAO.removeSciArticle(article)

    override fun exists(id: String): Boolean = userSciArticlesDAO.exists(id)

    override fun getUserSavedArticles(): List<SciArticle> = userSciArticlesDAO.userSavedArticles
}