package com.ilyasov.sci_king.presentation.view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilyasov.sci_king.model.SciArticle
import com.ilyasov.sci_king.retrofit.RetrofitInstance
import com.ilyasov.sci_king.util.Constants.Companion.LOG_RESPONSE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SciArticlesViewModel : ViewModel() {
    val sciArticlesListLiveData: MutableLiveData<List<SciArticle>> = MutableLiveData()
    val errorStateLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getSciArticlesByKeyWord(keyWord: String) = viewModelScope.launch(Dispatchers.Main) {
        loadingMutableLiveData.postValue(true)
        val response = RetrofitInstance.api.getSciArticlesByKeyWord("all:$keyWord")
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