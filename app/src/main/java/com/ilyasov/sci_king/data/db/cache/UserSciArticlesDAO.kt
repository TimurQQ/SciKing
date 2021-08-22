package com.ilyasov.sci_king.data.db.cache

import androidx.room.*
import com.ilyasov.sci_king.domain.entity.SciArticle

@Dao
interface UserSciArticlesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSciArticle(article: SciArticle)

    @Delete
    fun removeSciArticle(article: SciArticle)

    @Query("SELECT EXISTS (SELECT 1 FROM SciArticle WHERE id = :id)")
    fun exists(id: String): Boolean

    @get:Query("select * from SciArticle")
    val userSavedArticles: List<SciArticle>
}