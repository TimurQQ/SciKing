package com.ilyasov.sci_king.data.db.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ilyasov.sci_king.domain.entity.SciArticle

@Database(entities = [SciArticle::class], version = 1)
@TypeConverters(Converters::class)
abstract class SciArticlesDatabase : RoomDatabase() {
    abstract fun getUserSciArticlesDAO(): UserSciArticlesDAO
}