package com.ilyasov.sci_king.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.domain.interactor.usecase.firebase.GetCurrentUserUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.firebase.UploadToCloudUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.shared_prefs.PutDataToSharedPrefsUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.user_articles.GetSavedArticlesUseCase
import com.ilyasov.sci_king.util.Constants.Companion.FAILURE_MSG
import com.ilyasov.sci_king.util.Constants.Companion.SCI_ARTICLE_TO_READ
import com.ilyasov.sci_king.util.Constants.Companion.SUCCESS_MSG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavedArticlesViewModel @Inject constructor(
    private val getSavedArticlesUseCase: GetSavedArticlesUseCase,
    private val uploadToCloudUseCase: UploadToCloudUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val putDataToSharedPrefsUseCase: PutDataToSharedPrefsUseCase,
) : ViewModel() {
    val sciArticlesListLiveData: MutableLiveData<List<SciArticle>> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val callbackLiveData: MutableLiveData<String> = MutableLiveData()
    val navigationStateLiveData: MutableLiveData</*navHostFragment id*/ Int> = MutableLiveData()

    fun getSavedArticles() = viewModelScope.launch(Dispatchers.Main) {
        loadingMutableLiveData.postValue(true)
        sciArticlesListLiveData.postValue(getSavedArticlesUseCase.execute())
        loadingMutableLiveData.postValue(false)
    }

    fun startReadingArticle(sciArticle: SciArticle) {
        putDataToSharedPrefsUseCase.json(SCI_ARTICLE_TO_READ, sciArticle)
        navigationStateLiveData.postValue(R.id.navHostFragmentActivityRoot)
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