package com.ilyasov.sci_king.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.domain.interactor.usecase.AddSciArticleUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.ArticleExistUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.GetSciArticlesUseCase
import com.ilyasov.sci_king.util.Constants.Companion.LOG_RESPONSE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SciArticlesViewModel @Inject constructor(
    private val addSciArticleUseCase: AddSciArticleUseCase,
    private val articleExistUseCase: ArticleExistUseCase,
    private val getSciArticlesUseCase: GetSciArticlesUseCase
) : ViewModel() {

    val sciArticlesListLiveData: MutableLiveData<List<SciArticle>> = MutableLiveData()
    val errorStateLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun isArticleSaved(article: SciArticle, onCompleteCallback: (Boolean) -> Unit) =
        viewModelScope.launch(Dispatchers.Main) {
            val result = articleExistUseCase.execute(article.id)
            Log.d("TASK", "Completed")
            onCompleteCallback.invoke(result)
        }

    fun addSciArticleToLocalDB(article: SciArticle, onCompleteCallback: () -> Unit) =
        viewModelScope.launch(Dispatchers.Main) {
            addSciArticleUseCase.execute(article)
            Log.d("TASK", "Completed")
            onCompleteCallback.invoke()
        }

    fun getSciArticlesByKeyWord(keyWord: String) = viewModelScope.launch(Dispatchers.Main) {
        loadingMutableLiveData.postValue(true)
        val response = getSciArticlesUseCase.execute("all:$keyWord")
        loadingMutableLiveData.postValue(false)
        val body = response.body()

        Log.d(LOG_RESPONSE, body.toString())
        if (!response.isSuccessful) {
            errorStateLiveData.postValue("Error")
            return@launch
        } else {
            Log.d(LOG_RESPONSE, body.toString())
        }
        sciArticlesListLiveData.postValue(body!!.articles)
    }
}