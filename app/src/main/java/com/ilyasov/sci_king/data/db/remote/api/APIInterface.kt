package com.ilyasov.sci_king.data.db.remote.api

import com.ilyasov.sci_king.domain.entity.SciArticlesResponse
import com.ilyasov.sci_king.util.Constants.Companion.BASE_KEYWORD
import retrofit2.Response

interface APIInterface {

    companion object {
        const val START_POS = 0
        const val MAX_COUNT = 10
    }

    suspend fun getSciArticlesByKeyWord(
        keyword: String = "all:$BASE_KEYWORD",
        startPos: Int = START_POS,
        maxResults: Int = MAX_COUNT
    ): Response<SciArticlesResponse>

}