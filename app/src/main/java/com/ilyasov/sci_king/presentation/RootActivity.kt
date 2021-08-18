package com.ilyasov.sci_king.presentation

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.util.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sp = getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
        val theme = sp.getInt("THEME", R.style.AppTheme)
        setTheme(theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
    }
}
