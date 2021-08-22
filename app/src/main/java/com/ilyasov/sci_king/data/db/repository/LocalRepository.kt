package com.ilyasov.sci_king.data.db.repository

import com.ilyasov.sci_king.data.db.cache.UserSciArticlesDAO
import com.ilyasov.sci_king.domain.entity.SciArticle
import javax.inject.Inject

interface LocalRepository  {
    fun addSciArticle(article: SciArticle)
    fun removeSciArticle(article: SciArticle)
    fun exists(id: String): Boolean
    fun getUserSavedArticles(): List<SciArticle>
}