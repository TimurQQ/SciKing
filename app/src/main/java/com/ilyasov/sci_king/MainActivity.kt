package com.ilyasov.sci_king

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .add(R.id.container, SearchFragment()) // or replace
                .addToBackStack(null) // по нажатию "назад" можно вернуться на предыдущий фрагмент.
                .commit()
    }
}