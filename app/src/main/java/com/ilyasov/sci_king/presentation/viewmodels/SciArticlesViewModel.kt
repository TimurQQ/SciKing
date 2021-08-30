package com.ilyasov.sci_king.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.domain.interactor.usecase.firebase.GetCurrentUserUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.firebase.UploadToCloudUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.user_articles.AddSciArticleUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.user_articles.ArticleExistUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.user_articles.GetSciArticlesUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.user_articles.RemoveSciArticleUseCase
import com.ilyasov.sci_king.util.Constants.Companion.FAILURE_MSG
import com.ilyasov.sci_king.util.Constants.Companion.LOG_RESPONSE
import com.ilyasov.sci_king.util.Constants.Companion.RESPONSE_FAILED
import com.ilyasov.sci_king.util.Constants.Companion.START_LOADING
import com.ilyasov.sci_king.util.Constants.Companion.STOP_LOADING
import com.ilyasov.sci_king.util.Constants.Companion.SUCCESS_MSG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SciArticlesViewModel @Inject constructor(
    private val addSciArticleUseCase: AddSciArticleUseCase,
    private val removeSciArticleUseCase: RemoveSciArticleUseCase,
    private val articleExistUseCase: ArticleExistUseCase,
    private val getSciArticlesUseCase: GetSciArticlesUseCase,
    private val uploadToCloudUseCase: UploadToCloudUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {
    val sciArticlesListLiveData: MutableLiveData<MutableList<SciArticle>> = MutableLiveData()
    val errorStateLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val callbackLiveData: MutableLiveData<String> = MutableLiveData()

    fun isArticleSaved(article: SciArticle, onCompleteCallback: (Boolean) -> Unit) =
        viewModelScope.launch(Dispatchers.Main) {
            val result = articleExistUseCase.execute(article.id)
            onCompleteCallback.invoke(result)
        }

    fun addSciArticleToLocalDB(
        article: SciArticle, onCompleteCallback: (Boolean) -> Unit, adapterCallback :() -> Unit,
    ) =
        viewModelScope.launch(Dispatchers.Main) {
            addSciArticleUseCase.execute(article)
            onCompleteCallback.invoke(ARTICLE_ADDED)
            adapterCallback.invoke()
        }

    fun removeSciArticleFromLocalDB(
        article: SciArticle, onCompleteCallback: (Boolean) -> Unit, adapterCallback :() -> Unit,
    ) =
        viewModelScope.launch(Dispatchers.Main) {
            removeSciArticleUseCase.execute(article)
            onCompleteCallback.invoke(ARTICLE_DELETED)
            adapterCallback.invoke()
        }

    fun uploadToCloud(article: SciArticle) {
        uploadToCloudUseCase.execute(article).addOnFailureListener {
            callbackLiveData.postValue(FAILURE_MSG)
        }.addOnSuccessListener {
            callbackLiveData.postValue(SUCCESS_MSG)
        }
    }

    fun getCurrentUser(): FirebaseUser? = getCurrentUserUseCase.execute()

    fun getSciArticlesByKeyWord(keyWord: String) = viewModelScope.launch(Dispatchers.Main) {
        loadingMutableLiveData.postValue(START_LOADING)
        val response = getSciArticlesUseCase.execute("all:$keyWord")
        loadingMutableLiveData.postValue(STOP_LOADING)
        val body = response.body()

        if (!response.isSuccessful || body == null) {
            errorStateLiveData.postValue(RESPONSE_FAILED)
            return@launch
        } else {
            sciArticlesListLiveData.postValue(body.articles)
        }
    }

    companion object {
        const val ARTICLE_ADDED = true
        const val ARTICLE_DELETED = false
    }
}