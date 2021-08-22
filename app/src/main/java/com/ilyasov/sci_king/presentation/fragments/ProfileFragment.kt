package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_profile_container__button__open_auth_flow.setOnClickListener {
            Navigation.findNavController(
                requireActivity(),
                R.id.activity_root__fragment__nav_host
            ).navigate(R.id.action__MainFragment__to__AuthFlow)
        }
        if (mAuth.currentUser != null) {
            findNavController().navigate(R.id.action_ProfileFragment_to_UserProfileFragment)
        }
    }
}