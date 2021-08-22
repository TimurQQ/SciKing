package com.ilyasov.sci_king.data.db.repository

import com.ilyasov.sci_king.domain.entity.SciArticlesResponse
import retrofit2.Response

interface RemoteRepository {

    suspend fun getSciArticlesByKeyWord(
        keyword: String,
        startPos: Int,
        maxResults: Int
    ): Response<SciArticlesResponse>
}