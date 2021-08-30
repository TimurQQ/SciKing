package com.ilyasov.sci_king.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.domain.interactor.usecase.firebase.GetCurrentUserUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.firebase.UploadToCloudUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.user_articles.GetSavedArticlesUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.user_articles.RemoveSciArticleUseCase
import com.ilyasov.sci_king.util.Constants.Companion.FAILURE_MSG
import com.ilyasov.sci_king.util.Constants.Companion.START_LOADING
import com.ilyasov.sci_king.util.Constants.Companion.STOP_LOADING
import com.ilyasov.sci_king.util.Constants.Companion.SUCCESS_MSG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavedArticlesViewModel @Inject constructor(
    private val removeSciArticleUseCase: RemoveSciArticleUseCase,
    private val getSavedArticlesUseCase: GetSavedArticlesUseCase,
    private val uploadToCloudUseCase: UploadToCloudUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {
    val sciArticlesListLiveData: MutableLiveData<MutableList<SciArticle>> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val callbackLiveData: MutableLiveData<String> = MutableLiveData()

    fun getSavedArticles() = viewModelScope.launch(Dispatchers.Main) {
        loadingMutableLiveData.postValue(START_LOADING)
        sciArticlesListLiveData.postValue(getSavedArticlesUseCase.execute())
        loadingMutableLiveData.postValue(STOP_LOADING)
    }

    fun removeSciArticleFromLocalDB(
        article: SciArticle,
        timeDelay: Long,
        adapterCallback: () -> Unit,
    ) =
        viewModelScope.launch(Dispatchers.Main) {
            delay(timeDelay)
            removeSciArticleUseCase.execute(article)
            adapterCallback.invoke()
        }

    fun getCurrentUser(): FirebaseUser? = getCurrentUserUseCase.execute()

    fun uploadToCloud(article: SciArticle) {
        uploadToCloudUseCase.execute(article).addOnFailureListener {
            callbackLiveData.postValue(FAILURE_MSG)
        }.addOnSuccessListener {
            callbackLiveData.postValue(SUCCESS_MSG)
        }
    }
}