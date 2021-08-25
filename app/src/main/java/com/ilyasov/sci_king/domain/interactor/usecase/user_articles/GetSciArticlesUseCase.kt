package com.ilyasov.sci_king.domain.interactor.usecase.user_articles

import com.ilyasov.sci_king.data.db.repository.RemoteRepository
import com.ilyasov.sci_king.domain.entity.SciArticlesResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSciArticlesUseCase @Inject constructor(
    private val repo: RemoteRepository
) {
    suspend fun execute(keyword: String, startPos: Int = 0, maxResults: Int = 10)
            : Response<SciArticlesResponse> =
        repo.getSciArticlesByKeyWord(keyword, startPos, maxResults)
}