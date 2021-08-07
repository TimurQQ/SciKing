package com.ilyasov.sci_king.presentation.view_models

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilyasov.sci_king.model.SciArticle
import com.ilyasov.sci_king.retrofit.RetrofitInstance
import com.ilyasov.sci_king.room.SciArticlesDatabase
import com.ilyasov.sci_king.room.UserSciArticlesDAO
import com.ilyasov.sci_king.util.Constants.Companion.LOG_RESPONSE
import com.ilyasov.sci_king.util.OnTaskCompleted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SciArticlesViewModel(application: Application) : AndroidViewModel(application) {
    private var db: SciArticlesDatabase = SciArticlesDatabase.getInstance(application)
    private val sciArticlesDao: UserSciArticlesDAO = db.userSciArticlesDAO
    val sciArticlesListLiveData: MutableLiveData<List<SciArticle>> = MutableLiveData()
    val errorStateLiveData: MutableLiveData<String> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    companion object {
        private class InsertAsyncTask(dao: UserSciArticlesDAO, _listener: OnTaskCompleted) :
            AsyncTask<SciArticle, Void?, Void?>() {
            private val listener: OnTaskCompleted = _listener
            private val mAsyncTaskDao: UserSciArticlesDAO = dao

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                listener.onTaskCompleted()
            }

            override fun doInBackground(vararg params: SciArticle): Void? {
                params[0].let { mAsyncTaskDao.addSciArticle(it) }
                return null
            }
        }
    }

    fun insert(article: SciArticle) {
        InsertAsyncTask(sciArticlesDao, object : OnTaskCompleted {
            override fun onTaskCompleted() {
                Log.d("TASK", "Completed")
            }
        }).execute(article)
    }

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