package com.ilyasov.sci_king.data.db.repository

import com.ilyasov.sci_king.data.db.cache.UserSciArticlesDAO
import com.ilyasov.sci_king.data.db.remote.api.APIInterface
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.domain.entity.SciArticlesResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val sciArticlesAPI: APIInterface
) : RemoteRepository {

    override suspend fun getSciArticlesByKeyWord(
        keyword: String,
        startPos: Int,
        maxResults: Int
    ): Response<SciArticlesResponse> = sciArticlesAPI.getSciArticlesByKeyWord()
}