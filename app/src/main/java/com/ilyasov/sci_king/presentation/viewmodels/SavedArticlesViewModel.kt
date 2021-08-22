package com.ilyasov.sci_king.presentation.viewmodels

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.domain.interactor.usecase.GetSavedArticlesUseCase
import javax.inject.Inject

class SavedArticlesViewModel @Inject constructor(
    private val getSavedArticlesUseCase: GetSavedArticlesUseCase
) : ViewModel() {
    val sciArticlesListLiveData: MutableLiveData<List<SciArticle>> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    @SuppressLint("StaticFieldLeak")
    private inner class GetArticlesAsyncTask() :
        AsyncTask<Void?, Void?, Void?>() {

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            loadingMutableLiveData.postValue(false)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            sciArticlesListLiveData.postValue(getSavedArticlesUseCase.execute())
            return null
        }
    }

    fun getSavedArticles() {
        loadingMutableLiveData.postValue(true)
        GetArticlesAsyncTask().execute()
    }
}