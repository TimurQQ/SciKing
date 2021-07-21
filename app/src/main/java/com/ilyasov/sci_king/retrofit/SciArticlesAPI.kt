package com.ilyasov.sci_king.retrofit

import com.ilyasov.sci_king.model.SciArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SciArticlesAPI {

    //TODO use companion object for your constnts

    @GET("query/")
    suspend fun getSciArticlesByKeyWord(
        @Query("search_query")
        keyword: String = "all:electron"
    ) : Response<SciArticlesResponse>
}