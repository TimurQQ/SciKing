package com.ilyasov.sci_king.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.domain.interactor.usecase.GetSavedArticlesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavedArticlesViewModel @Inject constructor(
    private val getSavedArticlesUseCase: GetSavedArticlesUseCase
) : ViewModel() {
    val sciArticlesListLiveData: MutableLiveData<List<SciArticle>> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getSavedArticles() = viewModelScope.launch(Dispatchers.Main) {
        loadingMutableLiveData.postValue(true)
        sciArticlesListLiveData.postValue(getSavedArticlesUseCase.execute())
        loadingMutableLiveData.postValue(false)
    }
}