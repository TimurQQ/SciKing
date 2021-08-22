package com.ilyasov.sci_king.presentation.viewmodels

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.domain.interactor.usecase.AddSciArticleUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.ArticleExistUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.GetSciArticlesUseCase
import com.ilyasov.sci_king.util.Constants.Companion.LOG_RESPONSE
import com.ilyasov.sci_king.util.OnTaskCompleted
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

    @SuppressLint("StaticFieldLeak")
    inner class InsertAsyncTask(_listener: OnTaskCompleted) :
        AsyncTask<SciArticle, Void?, Void?>() {
        private val listener: OnTaskCompleted = _listener

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            listener.onTaskCompleted()
        }

        override fun doInBackground(vararg params: SciArticle): Void? {
            params[0].let { addSciArticleUseCase.execute(it) }
            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class IsArticleSavedAsyncTask(_listener: OnTaskCompleted) :
        AsyncTask<SciArticle, Void?, Boolean>() {
        private val listener: OnTaskCompleted = _listener

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            listener.onTaskCompleted(result)
        }

        override fun doInBackground(vararg params: SciArticle): Boolean {
            return articleExistUseCase.execute(params[0].id)
        }
    }

    fun isArticleSaved(article: SciArticle, onCompleteCallback: (Boolean) -> Unit) {
        IsArticleSavedAsyncTask(object : OnTaskCompleted {
            override fun onTaskCompleted() {}
            override fun onTaskCompleted(result: Boolean) {
                Log.d("TASK", "Completed")
                onCompleteCallback.invoke(result)
            }
        }).execute(article)
    }

    fun insert(article: SciArticle, onCompleteCallback: () -> Unit) {
        InsertAsyncTask(object : OnTaskCompleted {
            override fun onTaskCompleted(result: Boolean) {}
            override fun onTaskCompleted() {
                Log.d("TASK", "Completed")
                onCompleteCallback.invoke()
            }
        }).execute(article)
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