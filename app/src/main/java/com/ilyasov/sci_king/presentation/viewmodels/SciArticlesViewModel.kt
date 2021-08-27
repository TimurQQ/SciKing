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
            Log.d("TASK", "Completed")
            onCompleteCallback.invoke(result)
        }

    fun addSciArticleToLocalDB(
        article: SciArticle, onCompleteCallback: (Boolean) -> Unit, adapterCallback :() -> Unit,
    ) =
        viewModelScope.launch(Dispatchers.Main) {
            addSciArticleUseCase.execute(article)
            Log.d("TASK", "Completed")
            onCompleteCallback.invoke(true)
            adapterCallback.invoke()
        }

    fun removeSciArticleFromLocalDB(
        article: SciArticle, onCompleteCallback: (Boolean) -> Unit, adapterCallback :() -> Unit,
    ) =
        viewModelScope.launch(Dispatchers.Main) {
            removeSciArticleUseCase.execute(article)
            Log.d("TASK", "Completed")
            onCompleteCallback.invoke(false)
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