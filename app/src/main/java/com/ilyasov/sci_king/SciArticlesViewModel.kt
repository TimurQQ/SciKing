package com.ilyasov.sci_king

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilyasov.sci_king.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SciArticlesViewModel : ViewModel() {
    fun getSciArticlesByKeyWord(keyWord: String) = viewModelScope.launch(Dispatchers.Main) {
        val response = RetrofitInstance.api.getSciArticlesByKeyWord("all:$keyWord")
        val body = response.body()

        //TODO use const
        Log.d("RESP", body.toString())
        if (!response.isSuccessful) {
            return@launch
        } else {
            Log.d("RESP", body.toString())
        }
    }
}