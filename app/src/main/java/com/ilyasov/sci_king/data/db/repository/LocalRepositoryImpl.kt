package com.ilyasov.sci_king.data.db.repository

import android.content.SharedPreferences
import com.ilyasov.sci_king.data.db.cache.UserSciArticlesDAO
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.util.Constants
import com.ilyasov.sci_king.util.Constants.Companion.THEME_PREFS
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val userSciArticlesDAO: UserSciArticlesDAO,
    private val sharedPrefs: SharedPreferences,
) : LocalRepository {
    override suspend fun addSciArticle(article: SciArticle) =
        userSciArticlesDAO.addSciArticle(article)

    override suspend fun removeSciArticle(article: SciArticle) =
        userSciArticlesDAO.removeSciArticle(article)

    override suspend fun checkArticleExistence(id: String): Boolean =
        userSciArticlesDAO.checkArticleExistence(id)

    override suspend fun getUserSavedArticles(): List<SciArticle> =
        userSciArticlesDAO.getUserSavedArticles()

    override fun getStringFromSharedPrefs(key: String, defValue: String): String =
        sharedPrefs.getString(key, defValue).toString()

    override fun getIntFromSharedPrefs(key: String, defValue: Int): Int =
        sharedPrefs.getInt(key, defValue)

    override fun putStringToSharedPrefs(key: String, value: String) {
        sharedPrefs.edit().apply {
            putString(key, value)
            apply()
        }
    }

    override fun putIntToSharedPrefs(key: String, theme: Int) {
        sharedPrefs.edit().apply {
            putInt(key, theme)
            apply()
        }
    }
}