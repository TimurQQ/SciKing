package com.ilyasov.sci_king.data.db.repository

import com.ilyasov.sci_king.domain.entity.SciArticle

interface LocalRepository {
    suspend fun addSciArticle(article: SciArticle)
    suspend fun removeSciArticle(article: SciArticle)
    suspend fun checkArticleExistence(id: String): Boolean
    suspend fun getUserSavedArticles(): MutableList<SciArticle>

    fun getStringFromSharedPrefs(key: String, defValue: String): String
    fun getIntFromSharedPrefs(key:String, defValue: Int): Int
    fun putStringToSharedPrefs(key: String, value: String)
    fun putIntToSharedPrefs(key: String, value: Int)
}