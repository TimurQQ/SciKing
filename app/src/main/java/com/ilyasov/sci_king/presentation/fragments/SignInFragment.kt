package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.presentation.view_models.SignInViewModel
import com.ilyasov.sci_king.util.Constants.Companion.EMAIL_CHECK_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.PASSWORD_CHECK_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.SERVER_SIGN_IN_ERROR
import com.ilyasov.sci_king.util.isVisible
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private val viewModel: SignInViewModel by lazy { SignInViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signInOnClickListener = View.OnClickListener { clickableView ->
            when (clickableView.id) {
                R.id.textViewSignUp ->
                    findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
                R.id.buttonLogin -> {
                    val email = editTextEmail.text.toString().trim { it <= ' ' }
                    val password: String = editTextPassword.text.toString().trim { it <= ' ' }
                    viewModel.userLogin(email, password)
                }
            }
        }
        textViewSignUp.setOnClickListener(signInOnClickListener)
        buttonLogin.setOnClickListener(signInOnClickListener)

        viewModel.errorStateLiveData.observe(viewLifecycleOwner, { login_error ->
            when (login_error.first) {
                EMAIL_CHECK_ERROR -> editTextEmail
                PASSWORD_CHECK_ERROR -> editTextPassword
                else -> null
            }?.apply {
                error = login_error.second
                requestFocus()
            }
            when (login_error.first) {
                SERVER_SIGN_IN_ERROR ->
                    Toast.makeText(context, login_error.second, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.loadingMutableLiveData.observe(viewLifecycleOwner, { visibility ->
            progressBar.isVisible(visibility)
        })
        viewModel.navigationStateLiveData.observe(viewLifecycleOwner, { action ->
            findNavController().navigate(action)
        })
    }
}