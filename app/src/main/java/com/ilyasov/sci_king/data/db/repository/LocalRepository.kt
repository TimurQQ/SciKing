package com.ilyasov.sci_king.data.db.repository

import com.ilyasov.sci_king.domain.entity.SciArticle

interface LocalRepository {
    suspend fun addSciArticle(article: SciArticle)
    suspend fun removeSciArticle(article: SciArticle)
    suspend fun checkArticleExistence(id: String): Boolean
    suspend fun getUserSavedArticles(): List<SciArticle>
}