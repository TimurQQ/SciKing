package com.ilyasov.sci_king.domain.interactor.usecase.shared_prefs

import com.google.gson.Gson
import com.ilyasov.sci_king.data.db.repository.LocalRepository
import com.ilyasov.sci_king.domain.entity.SciArticle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFromSharedPrefsUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val gson: Gson,
) {
    fun sciArticle(key: String, defValue: String): SciArticle {
        val dataString = localRepository.getStringFromSharedPrefs(key, defValue)
        return gson.fromJson(dataString, SciArticle::class.java)
    }

    fun int(key: String, defValue: Int): Int =
        localRepository.getIntFromSharedPrefs(key, defValue)
}