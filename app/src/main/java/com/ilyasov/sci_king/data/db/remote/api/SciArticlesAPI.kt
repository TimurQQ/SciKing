package com.ilyasov.sci_king.data.db.remote.api

import com.ilyasov.sci_king.domain.entity.SciArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SciArticlesAPI : APIInterface {

    companion object {
        const val SET_START_POS = "start"
        const val GET_ARTICLES = "query"
        const val SET_SEARCH_KEYWORD = "search_query"
        const val SET_MAX_RESULTS = "max_results"
    }

    @GET(GET_ARTICLES)
    override suspend fun getSciArticlesByKeyWord(
        @Query(SET_SEARCH_KEYWORD)
        keyword: String,
        @Query(SET_START_POS)
        startPos: Int,
        @Query(SET_MAX_RESULTS)
        maxResults: Int,
    ): Response<SciArticlesResponse>
}