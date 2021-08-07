package com.ilyasov.sci_king.presentation.view_models

import android.annotation.SuppressLint
import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ilyasov.sci_king.model.SciArticle
import com.ilyasov.sci_king.room.SciArticlesDatabase
import com.ilyasov.sci_king.room.UserSciArticlesDAO

class SavedArticlesViewModel(application: Application) : AndroidViewModel(application) {
    private var db: SciArticlesDatabase = SciArticlesDatabase.getInstance(application)
    private val sciArticlesDao: UserSciArticlesDAO = db.userSciArticlesDAO
    val sciArticlesListLiveData: MutableLiveData<List<SciArticle>> = MutableLiveData()
    val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    @SuppressLint("StaticFieldLeak")
    private inner class GetArticlesAsyncTask(dao: UserSciArticlesDAO) :
        AsyncTask<Void?, Void?, Void?>() {
        private val mAsyncTaskDao: UserSciArticlesDAO = dao

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            loadingMutableLiveData.postValue(false)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            sciArticlesListLiveData.postValue(mAsyncTaskDao.userSavedArticles)
            return null
        }
    }

    fun getSavedArticles() {
        loadingMutableLiveData.postValue(true)
        GetArticlesAsyncTask(sciArticlesDao).execute()
    }
}