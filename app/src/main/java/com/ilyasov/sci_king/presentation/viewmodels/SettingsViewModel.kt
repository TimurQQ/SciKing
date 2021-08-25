package com.ilyasov.sci_king.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.domain.interactor.usecase.shared_prefs.GetFromSharedPrefsUseCase
import com.ilyasov.sci_king.domain.interactor.usecase.shared_prefs.PutDataToSharedPrefsUseCase
import com.ilyasov.sci_king.util.Constants.Companion.THEME_PREFS
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getFromSharedPrefsUseCase: GetFromSharedPrefsUseCase,
    private val putDataToSharedPrefsUseCase: PutDataToSharedPrefsUseCase,
) : ViewModel() {
    fun getGlobalTheme(): Int = getFromSharedPrefsUseCase.int(THEME_PREFS, R.style.AppTheme)
    fun setGlobalTheme(theme: Int) = putDataToSharedPrefsUseCase.int(THEME_PREFS, theme)
}