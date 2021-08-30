package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.util.makeDisappear
import kotlinx.android.synthetic.main.fragment_splash_screen.*

class SplashScreenFragment : BaseFragment(R.layout.fragment_splash_screen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgAppIcon.makeDisappear(2000) {
            findNavController().navigate(R.id.action_SplashScreenFragment_to_MainFragment)
        }
    }
}