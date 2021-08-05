package com.ilyasov.sci_king.presentation.fragments

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.ilyasov.sci_king.R


class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val iconView: ImageView = view.findViewById(R.id.app_icon)
        iconView.animate().alpha(0f).setDuration(2000)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    val actionId = if (mAuth.currentUser != null)
                        R.id.action_splashScreenFragment_to_appActivity
                    else
                        R.id.action_splashScreenFragment_to_signInFragment
                    findNavController().navigate(actionId)
                }
            })
    }
}