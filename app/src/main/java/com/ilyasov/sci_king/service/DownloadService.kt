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
import com.ilyasov.sci_king.SciKingApplication
import com.ilyasov.sci_king.presentation.viewmodels.ParseArticleViewModel
import com.ilyasov.sci_king.presentation.viewmodels.ParseArticleViewModel.Companion.file
import com.ilyasov.sci_king.util.Constants
import com.ilyasov.sci_king.util.Constants.Companion.DOWNLOAD_URL_INFO
import com.ilyasov.sci_king.util.Constants.Companion.FILENAME_INFO
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
        SciKingApplication.appComponent.inject(this)
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
        val fileName = intent.getStringExtra(FILENAME_INFO)
        val url = intent.getStringExtra(DOWNLOAD_URL_INFO)
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
        val pdfPath = getExternalFilesDir(USER_SAVED_ARTICLES_PATH)?.absolutePath
        downloadCount.postValue(downloadCount.value?.plus(1))
        CoroutineScope(Dispatchers.IO).launch {
            PRDownloader.download(url, pdfPath, "$name.pdf")
                .build()
                .setOnProgressListener { progress ->
                    downloadingItems.postValue(mutableMapOf(name to progress))
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        onDownloadComplete(pdfPath, name)
                    }

                    override fun onError(error: Error) {
                        Log.e(ParseArticleViewModel.TAG, "$error")
                    }
                })
        }
    }

    private fun onDownloadComplete(pdfPath: String?, fileName: String) {
        downloadCount.postValue(downloadCount.value?.minus(1))
        downloadingItems.value?.remove(fileName)
        val fileUri = FileProvider.getUriForFile(
            this@DownloadService,
            PACKAGE_NAME,
            File("$pdfPath/$fileName.pdf")
        )
        file.postValue(fileUri)
        if (downloadingItems.value.isNullOrEmpty())
            stopService()
    }

    private fun stopService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(true)
        stopSelf()
    }
}