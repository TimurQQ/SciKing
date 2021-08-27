package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ilyasov.sci_king.BR
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.databinding.FragmentProfileBinding
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.presentation.viewmodels.ProfileViewModel

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {
    private val viewModel: ProfileViewModel by lazy { createViewModel {} }
    lateinit var fragmentProfileBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        fragmentProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        fragmentProfileBinding.setVariable(BR.profileFragment, this)
        return fragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.getCurrentUser() != null) {
            findNavController().navigate(R.id.action_ProfileFragment_to_UserProfileFragment)
        }
    }

    fun onClickAuthFlowButton() {
        Navigation.findNavController(
            requireActivity(),
            R.id.navHostFragmentActivityRoot
        ).navigate(R.id.action__MainFragment__to__AuthFlow)
    }
}