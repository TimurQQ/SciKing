package com.ilyasov.sci_king

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.ilyasov.sci_king.presentation.di.*
import com.ilyasov.sci_king.util.Constants.Companion.CHANNEL_ID
import com.ilyasov.sci_king.util.Constants.Companion.CHANNEL_NAME

class SciKingApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
        initPRDownloader()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
            val mNotifyManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotifyManager.createNotificationChannel(channel)
        }
    }

    private fun initPRDownloader() {
        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(this, config)
    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .localModule(LocalModule())
            .remoteModule(RemoteModule())
            .notificationModule(NotificationModule())
            .firebaseModule(FirebaseModule())
            .build()
    }

}