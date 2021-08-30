package com.ilyasov.sci_king.domain.interactor.usecase.shared_prefs

import com.ilyasov.sci_king.data.db.repository.LocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PutDataToSharedPrefsUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {
    fun int(key: String, value: Int) = localRepository.putIntToSharedPrefs(key, value)
}