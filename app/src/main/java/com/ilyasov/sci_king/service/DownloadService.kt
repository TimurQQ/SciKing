package com.ilyasov.sci_king.service

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleService
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.ilyasov.sci_king.presentation.viewmodels.ParseArticleViewModel
import com.ilyasov.sci_king.presentation.viewmodels.ParseArticleViewModel.Companion.file
import com.ilyasov.sci_king.util.Constants.Companion.MAX_PROGRESS
import com.ilyasov.sci_king.util.Constants.Companion.NOTIFICATION_ID
import com.ilyasov.sci_king.util.Constants.Companion.PACKAGE_NAME
import com.ilyasov.sci_king.util.Constants.Companion.USER_SAVED_ARTICLES_PATH
import com.ilyasov.sci_king.util.StaticVariables.downloadCount
import com.ilyasov.sci_king.util.StaticVariables.downloadingItems
import com.ilyasov.sci_king.util.calculateProgress
import com.ilyasov.sci_king.util.calculateProgressString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class DownloadService : LifecycleService() {
    @Inject
    lateinit var mManager: NotificationCompat.Builder

    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent.apply {
            setupObserver()
            setupDownload(this)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun setupDownload(intent: Intent) {
        val fileName = intent.getStringExtra("file_name")
        val url = intent.getStringExtra("url")
        fileName?.let {
            downloadPdf(url, fileName)
        }
    }

    private fun setupObserver() {
        downloadingItems.observe(this, { item ->
            item.forEach {
                val notification = mManager
                    .setContentTitle(it.key)
                    .setProgress(MAX_PROGRESS, it.value.calculateProgress(), false)
                    .setContentText(it.value.calculateProgressString())
                    .build()

                startForeground(NOTIFICATION_ID, notification)
            }
        })
    }

    private fun downloadPdf(url: String?, name: String) {
        val pdfPath = this.getExternalFilesDir(USER_SAVED_ARTICLES_PATH)?.absolutePath
        downloadCount.postValue(downloadCount.value?.plus(1))
        CoroutineScope(Dispatchers.IO).launch {
            PRDownloader.download(url, pdfPath, "$name.pdf")
                .build()
                .setOnProgressListener { progress -> downloadingItems.postValue(mutableMapOf(name to progress)) }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        downloadCount.postValue(downloadCount.value?.minus(1))
                        downloadingItems.value?.remove(name)
                        val fileUri = FileProvider.getUriForFile(
                            this@DownloadService,
                            PACKAGE_NAME,
                            File("$pdfPath/$name.pdf")
                        )
                        file.postValue(fileUri)
                        if (downloadingItems.value.isNullOrEmpty())
                            stopService()
                    }

                    override fun onError(error: Error) {
                        Log.e(ParseArticleViewModel.TAG, "$error")
                    }
                })
        }
    }

    private fun stopService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(true)
        stopSelf()
    }
}