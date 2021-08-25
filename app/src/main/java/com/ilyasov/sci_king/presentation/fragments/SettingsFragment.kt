package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.ilyasov.sci_king.BR
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.databinding.FragmentSettingsBinding
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.presentation.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {
    private lateinit var fragmentSettingsBinding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by lazy { createViewModel {} }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        fragmentSettingsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        fragmentSettingsBinding.setVariable(BR.settingsFragment, this)
        return fragmentSettingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val theme = viewModel.getGlobalTheme()
        switchThemeLightDark.isChecked = theme == R.style.DarkTheme
        switchThemeLightDark.setOnCheckedChangeListener { _, isChecked ->
            onSwitchCheckChange(isChecked)
        }
    }

    private fun onSwitchCheckChange(isChecked: Boolean) {
        val theme = when (isChecked) {
            true -> R.style.DarkTheme
            false -> R.style.AppTheme
        }
        viewModel.setGlobalTheme(theme)
        requireActivity().recreate()
    }

    fun onClickBack() {
        Navigation.findNavController(
            requireActivity(),
            R.id.navHostFragmentActivityRoot
        ).popBackStack(R.id.change_theme_nav_graph, true)
    }
}