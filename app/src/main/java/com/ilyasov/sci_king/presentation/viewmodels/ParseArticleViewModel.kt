package com.ilyasov.sci_king.presentation.viewmodels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.domain.interactor.usecase.shared_prefs.GetFromSharedPrefsUseCase
import com.ilyasov.sci_king.service.DownloadService
import com.ilyasov.sci_king.util.Constants.Companion.DOWNLOAD_URL_INFO
import com.ilyasov.sci_king.util.Constants.Companion.FILENAME_INFO
import com.ilyasov.sci_king.util.Constants.Companion.NULL_STRING
import com.ilyasov.sci_king.util.Constants.Companion.SCI_ARTICLE_TO_READ
import com.ilyasov.sci_king.util.Constants.Companion.USER_SAVED_ARTICLES_PATH
import java.io.File
import javax.inject.Inject

class ParseArticleViewModel @Inject constructor(
    private val getFromSharedPrefsUseCase: GetFromSharedPrefsUseCase,
) : ViewModel() {
    companion object {
        const val TAG = "Read Article View Model"
        val file = MutableLiveData<Uri>()
    }

    fun getCurrentBook(): SciArticle =
        getFromSharedPrefsUseCase.sciArticle(SCI_ARTICLE_TO_READ, NULL_STRING)

    fun getBookData(url: String, name: String, context: Context) {
        val pdfPath = context.getExternalFilesDir(USER_SAVED_ARTICLES_PATH)?.absolutePath
        val filePath = File("$pdfPath/$name.pdf")
        if (!filePath.exists()) {
            Intent(context, DownloadService::class.java).apply {
                putExtra(DOWNLOAD_URL_INFO, url)
                putExtra(FILENAME_INFO, name)
                context.startService(this)
            }
        } else {
            file.postValue(filePath.toUri())
        }
    }
}