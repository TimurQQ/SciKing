package com.ilyasov.sci_king.presentation

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.util.Constants.Companion.APP_PREFERENCES
import com.ilyasov.sci_king.util.Constants.Companion.THEME_PREFS

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPrefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val theme = sharedPrefs.getInt(THEME_PREFS, R.style.AppTheme)
        setTheme(theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
    }
}
