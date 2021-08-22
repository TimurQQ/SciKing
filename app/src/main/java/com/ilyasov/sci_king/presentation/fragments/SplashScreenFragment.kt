package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.util.makeDisappear

class SplashScreenFragment : BaseFragment(R.layout.fragment_splash_screen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val iconView: ImageView = view.findViewById(R.id.app_icon)
        iconView.makeDisappear(2000) {
            findNavController().navigate(R.id.action_SplashScreenFragment_to_MainFragment)
        }
    }
}