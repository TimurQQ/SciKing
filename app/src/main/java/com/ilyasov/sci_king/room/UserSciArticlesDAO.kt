package com.ilyasov.sci_king.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ilyasov.sci_king.model.SciArticle

@Dao
interface UserSciArticlesDAO {
    @Insert
    fun addSciArticle(article: SciArticle)

    @Delete
    fun removeSciArticle(article: SciArticle)

    @get:Query("select * from SciArticle")
    val userSavedArticles: List<SciArticle>
}