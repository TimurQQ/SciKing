package com.ilyasov.sci_king.presentation.viewmodels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilyasov.sci_king.service.DownloadService
import com.ilyasov.sci_king.util.Constants.Companion.USER_SAVED_ARTICLES_PATH
import java.io.File
import javax.inject.Inject

class ParseArticleViewModel @Inject constructor() : ViewModel() {
    fun getBookData(url: String, name: String, context: Context) {
        val pdfPath = context.getExternalFilesDir(USER_SAVED_ARTICLES_PATH)?.absolutePath
        val filePath = File("$pdfPath/$name.pdf")
        if (!filePath.exists()) {
            // If file not exist than start download service
            Intent(context, DownloadService::class.java).apply {
                putExtra("url", url)
                putExtra("file_name", name)
                context.startService(this)
            }
        } else {
            file.postValue(filePath.toUri())
        }
    }

    companion object {
        const val TAG = "Read Book View Model"

        // This is temporary solution.
        // I declare like this because i wanna reach this variable inside download service.
        val file = MutableLiveData<Uri>()
    }
}
