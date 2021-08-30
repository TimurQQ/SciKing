package com.ilyasov.sci_king.presentation.di

import com.ilyasov.sci_king.data.db.cache.Converters
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.service.DownloadService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RemoteModule::class,
        LocalModule::class,
        ViewModelModule::class,
        NotificationModule::class,
        FirebaseModule::class
    ]
)

interface AppComponent {
    fun inject(baseFragment: BaseFragment)
    fun inject(service: DownloadService)
    fun inject(convertersClass: Converters)
}