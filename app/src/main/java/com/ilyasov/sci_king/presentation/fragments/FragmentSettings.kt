package com.ilyasov.sci_king.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.util.Constants

class FragmentSettings : Fragment(R.layout.fragment_settings) {
    private lateinit var sharedPrefs: SharedPreferences
    private var switchCompat: SwitchCompat? = null
    private lateinit var backButton: AppCompatImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton = view.findViewById(R.id.jump)

        sharedPrefs =
            requireContext().getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)

        switchCompat = view.findViewById(R.id.on_off)
        switchCompat?.isChecked = sharedPrefs.getInt("THEME", R.style.AppTheme) == R.style.DarkTheme
        switchCompat?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPrefs.edit().apply {
                    putInt("THEME", R.style.DarkTheme)
                    apply()
                }
            }
            else {
                sharedPrefs.edit().apply {
                    putInt("THEME", R.style.AppTheme)
                    apply()
                }
            }
            requireActivity().recreate()
        }
        backButton.setOnClickListener {
            Navigation.findNavController(
                requireActivity(),
                R.id.activity_root__fragment__nav_host
            ).popBackStack(R.id.change_theme_nav_graph, true)
        }
    }
}