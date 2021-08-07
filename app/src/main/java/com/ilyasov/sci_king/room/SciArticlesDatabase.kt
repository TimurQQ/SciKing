package com.ilyasov.sci_king.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ilyasov.sci_king.model.SciArticle
import com.ilyasov.sci_king.util.SingletonHolder

@Database(entities = [SciArticle::class], version = 1)
@TypeConverters(Converters::class)
abstract class SciArticlesDatabase : RoomDatabase() {
    abstract val userSciArticlesDAO: UserSciArticlesDAO

    companion object : SingletonHolder<SciArticlesDatabase, Context>({ context ->
        databaseBuilder(
            context.applicationContext,
            SciArticlesDatabase::class.java,
            "user sci-articles database"
        ).build()
    })
}