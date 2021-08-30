package com.ilyasov.sci_king.presentation.di

import com.ilyasov.sci_king.data.db.remote.api.APIInterface
import com.ilyasov.sci_king.data.db.remote.api.SciArticlesAPI
import com.ilyasov.sci_king.data.db.repository.RemoteRepository
import com.ilyasov.sci_king.data.db.repository.RemoteRepositoryImpl
import com.ilyasov.sci_king.util.Constants.Companion.BASE_URL
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RemoteModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(TikXmlConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIInterface(retrofit: Retrofit): APIInterface =
        retrofit.create(SciArticlesAPI::class.java)

    @Singleton
    @Provides
    fun providesRemoteRepository(service: APIInterface): RemoteRepository =
        RemoteRepositoryImpl(service)

}