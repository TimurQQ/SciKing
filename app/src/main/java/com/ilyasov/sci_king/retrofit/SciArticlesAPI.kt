package com.ilyasov.sci_king.retrofit

import com.ilyasov.sci_king.model.SciArticlesResponse
import com.ilyasov.sci_king.util.Constants.Companion.BASE_KEYWORD
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SciArticlesAPI {

    @GET("query")
    suspend fun getSciArticlesByKeyWord(
        @Query("search_query")
        keyword: String = "all:$BASE_KEYWORD",
        @Query("start")
        startPos: Int = 0,
        @Query("max_results")
        maxResults: Int = 10
    ) : Response<SciArticlesResponse>
}