package com.ilyasov.sci_king.presentation.di

import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RemoteModule::class, LocalModule::class, ViewModelModule::class])

interface AppComponent {
    fun inject(baseFragment: BaseFragment)
}