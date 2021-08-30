package com.ilyasov.sci_king.presentation.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.presentation.RootActivity
import com.ilyasov.sci_king.util.Constants.Companion.CHANNEL_ID
import com.ilyasov.sci_king.util.Constants.Companion.DOWNLOADING_CONTENT_TITLE
import com.ilyasov.sci_king.util.Constants.Companion.PENDING_CODE
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotificationModule {
    @Singleton
    @Provides
    fun provideNotificationManager(
        context: Context,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder = NotificationCompat.Builder(
        context,
        CHANNEL_ID
    ).setSmallIcon(R.drawable.ic_download)
        .setAutoCancel(false)
        .setOngoing(false)
        .setContentTitle(DOWNLOADING_CONTENT_TITLE)
        .setContentIntent(pendingIntent)

    @Singleton
    @Provides
    fun providePendingIntent(
        context: Context,
        intent: Intent
    ): PendingIntent =
        PendingIntent.getActivity(
            context,
            PENDING_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    @Singleton
    @Provides
    fun provideIntent(
        context: Context
    ): Intent = Intent(context, RootActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    }
}