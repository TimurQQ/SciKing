package com.ilyasov.sci_king

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ilyasov.sci_king.util.Constants.Companion.UNCHECKED_CAST
import com.ilyasov.sci_king.util.Constants.Companion.UNKNOWN_MODEL_CLASS
import javax.inject.Inject
import javax.inject.Provider

class ViewModelProviderFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
    ViewModelProvider.Factory {
    @Suppress(UNCHECKED_CAST)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException(UNKNOWN_MODEL_CLASS + modelClass)
        return creator.get() as T
    }
}