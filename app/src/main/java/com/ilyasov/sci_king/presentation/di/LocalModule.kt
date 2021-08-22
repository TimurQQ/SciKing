package com.ilyasov.sci_king.presentation.di

import android.content.Context
import androidx.room.Room
import com.ilyasov.sci_king.data.db.cache.SciArticlesDatabase
import com.ilyasov.sci_king.data.db.cache.UserSciArticlesDAO
import com.ilyasov.sci_king.data.db.repository.LocalRepository
import com.ilyasov.sci_king.data.db.repository.LocalRepositoryImpl
import com.ilyasov.sci_king.util.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {

    @Singleton
    @Provides
    fun providesLocalRepository(dao: UserSciArticlesDAO): LocalRepository =
        LocalRepositoryImpl(dao)

    @Singleton
    @Provides
    fun provideDatabase(context: Context): SciArticlesDatabase =
        Room.databaseBuilder(
            context,
            SciArticlesDatabase::class.java,
            Constants.SCI_ARTICLES_DB_NAME
        ).build()

    @Singleton
    @Provides
    fun provideMoviesDao(db: SciArticlesDatabase): UserSciArticlesDAO =
        db.getUserSciArticlesDAO()

}