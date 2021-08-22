package com.ilyasov.sci_king.presentation.fragments.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ilyasov.sci_king.SciKingApplication
import javax.inject.Inject

open class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SciKingApplication.appComponent.inject(this)
    }

    inline fun <reified T : ViewModel> createViewModel(body: T.() -> Unit): T {
        val vm = ViewModelProvider(this, viewModelProvider)[T::class.java]
        vm.body()
        return vm
    }
}