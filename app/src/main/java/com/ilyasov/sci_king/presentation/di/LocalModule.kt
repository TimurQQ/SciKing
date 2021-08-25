package com.ilyasov.sci_king.presentation.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.ilyasov.sci_king.data.db.cache.SciArticlesDatabase
import com.ilyasov.sci_king.data.db.cache.UserSciArticlesDAO
import com.ilyasov.sci_king.data.db.repository.LocalRepository
import com.ilyasov.sci_king.data.db.repository.LocalRepositoryImpl
import com.ilyasov.sci_king.util.Constants.Companion.APP_PREFERENCES
import com.ilyasov.sci_king.util.Constants.Companion.SCI_ARTICLES_DB_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {

    @Singleton
    @Provides
    fun providesLocalRepository(
        dao: UserSciArticlesDAO,
        sharePrefs: SharedPreferences,
    ): LocalRepository =
        LocalRepositoryImpl(dao, sharePrefs)

    @Singleton
    @Provides
    fun provideDatabase(context: Context): SciArticlesDatabase =
        Room.databaseBuilder(
            context,
            SciArticlesDatabase::class.java,
            SCI_ARTICLES_DB_NAME
        ).build()

    @Singleton
    @Provides
    fun provideMoviesDao(db: SciArticlesDatabase): UserSciArticlesDAO =
        db.getUserSciArticlesDAO()

    @Singleton
    @Provides
    fun provideSharedPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

}