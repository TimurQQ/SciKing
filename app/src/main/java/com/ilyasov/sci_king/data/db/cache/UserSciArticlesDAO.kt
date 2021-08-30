package com.ilyasov.sci_king.data.db.cache

import androidx.room.*
import com.ilyasov.sci_king.domain.entity.SciArticle

@Dao
interface UserSciArticlesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSciArticle(article: SciArticle)

    @Delete
    suspend fun removeSciArticle(article: SciArticle)

    @Query("SELECT EXISTS (SELECT 1 FROM SciArticle WHERE id = :id)")
    suspend fun checkArticleExistence(id: String): Boolean

    @Query("select * from SciArticle")
    suspend fun getUserSavedArticles(): List<SciArticle>
}