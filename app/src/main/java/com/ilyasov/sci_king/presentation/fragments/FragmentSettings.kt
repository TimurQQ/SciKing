package com.ilyasov.sci_king.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
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
import com.ilyasov.sci_king.util.Constants.Companion.APP_PREFERENCES
import com.ilyasov.sci_king.util.Constants.Companion.THEME_PREFS
import kotlinx.android.synthetic.main.fragment_settings.*

class FragmentSettings : BaseFragment(R.layout.fragment_settings) {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var fragmentSettingsBinding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
    fragmentSettingsBinding.setVariable(BR.settingsFragment, this)
        return fragmentSettingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefs =
            requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        on_off.isChecked = sharedPrefs.getInt(THEME_PREFS, R.style.AppTheme) == R.style.DarkTheme
        on_off.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().apply {
                val theme = when (isChecked) {
                    true -> R.style.DarkTheme
                    false -> R.style.AppTheme
                }
                putInt(THEME_PREFS, theme)
                apply()
            }
            requireActivity().recreate()
        }
    }

    fun onClickBack() {
        Navigation.findNavController(
            requireActivity(),
            R.id.activity_root__fragment__nav_host
        ).popBackStack(R.id.change_theme_nav_graph, true)
    }
}