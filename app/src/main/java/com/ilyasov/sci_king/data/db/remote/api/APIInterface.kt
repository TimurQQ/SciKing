package com.ilyasov.sci_king.data.db.remote.api

import com.ilyasov.sci_king.domain.entity.SciArticlesResponse
import com.ilyasov.sci_king.util.Constants.Companion.BASE_KEYWORD
import retrofit2.Response

interface APIInterface {

    suspend fun getSciArticlesByKeyWord(
        keyword: String = "all:$BASE_KEYWORD",
        startPos: Int = 0,
        maxResults: Int = 10
    ): Response<SciArticlesResponse>

}