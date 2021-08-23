package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ilyasov.sci_king.BR
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.databinding.FragmentSignUpBinding
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.presentation.viewmodels.SignUpViewModel
import com.ilyasov.sci_king.util.Constants.Companion.EMAIL_CHECK_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.PASSWORD_CHECK_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.SERVER_SIGN_UP_ERROR
import com.ilyasov.sci_king.util.isVisible
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {
    private val viewModel: SignUpViewModel by lazy { createViewModel {} }
    private lateinit var fragmentSignUpBinding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        fragmentSignUpBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        fragmentSignUpBinding.setVariable(BR.signUpFragment, this)
        return fragmentSignUpBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.errorStateLiveData.observe(viewLifecycleOwner, { sign_up_error ->
            handleSignUpError(sign_up_error)
        })
        viewModel.loadingMutableLiveData.observe(viewLifecycleOwner, { visibility ->
            progressBar.isVisible(visibility)
        })
        viewModel.navigationStateLiveData.observe(viewLifecycleOwner, { navHostFragmentId ->
            Navigation.findNavController(
                requireActivity(),
                navHostFragmentId
            ).popBackStack(R.id.auth_nav_graph, true)
        })
    }

    private fun handleSignUpError(error_type_and_error_text: Pair<String, String>) {
        when (error_type_and_error_text.first) {
            EMAIL_CHECK_ERROR -> edtEmail
            PASSWORD_CHECK_ERROR -> edtPassword
            else -> null
        }?.apply {
            error = error_type_and_error_text.second
            requestFocus()
        }
        when (error_type_and_error_text.first) {
            SERVER_SIGN_UP_ERROR ->
                Toast.makeText(context, error_type_and_error_text.second, Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickSignUp() {
        val email = edtEmail.text.toString().trim { it <= ' ' }
        val password: String = edtPassword.text.toString().trim { it <= ' ' }
        viewModel.registerUser(email, password)
    }

    fun onCLickLogin() {
        findNavController().popBackStack()
    }
}