package com.ilyasov.sci_king.presentation.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.ilyasov.sci_king.data.db.repository.FirebaseRepository
import com.ilyasov.sci_king.data.db.repository.FirebaseRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseRepository(
        firebaseAuth: FirebaseAuth,
        firebaseStorage: FirebaseStorage,
        gson: Gson,
    ): FirebaseRepository =
        FirebaseRepositoryImpl(firebaseAuth, firebaseStorage, gson)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()
}