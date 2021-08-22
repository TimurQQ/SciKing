package com.ilyasov.sci_king.data.db.remote.api

import com.ilyasov.sci_king.domain.entity.SciArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SciArticlesAPI : APIInterface {

    @GET("query")
   override suspend fun getSciArticlesByKeyWord(
        @Query("search_query")
        keyword: String,
        @Query("start")
        startPos: Int,
        @Query("max_results")
        maxResults: Int): Response<SciArticlesResponse>
}