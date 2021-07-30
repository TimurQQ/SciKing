package com.ilyasov.sci_king

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO use navigation controller (navGraph)
        supportFragmentManager.beginTransaction()
                .add(R.id.container, SearchFragment()) // or replace
                .addToBackStack(null) // by clicking "back", you can return to the previous fragment.
                .commit()
    }
}