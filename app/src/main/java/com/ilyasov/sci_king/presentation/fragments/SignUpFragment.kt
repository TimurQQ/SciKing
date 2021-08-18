package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.presentation.view_models.SignUpViewModel
import com.ilyasov.sci_king.util.Constants.Companion.EMAIL_CHECK_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.PASSWORD_CHECK_ERROR
import com.ilyasov.sci_king.util.Constants.Companion.SERVER_SIGN_UP_ERROR
import com.ilyasov.sci_king.util.isVisible
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private val viewModel: SignUpViewModel by lazy { SignUpViewModel() }
    private lateinit var navController : NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        val signUpOnClickListener = View.OnClickListener { clickableView ->
            when (clickableView.id) {
                R.id.buttonSignUp -> {
                    val email = editTextEmail.text.toString().trim { it <= ' ' }
                    val password: String = editTextPassword.text.toString().trim { it <= ' ' }
                    viewModel.registerUser(email, password)
                }
                R.id.textViewLogin -> navController.popBackStack()
            }
        }
        buttonSignUp.setOnClickListener(signUpOnClickListener)
        textViewLogin.setOnClickListener(signUpOnClickListener)

        viewModel.errorStateLiveData.observe(viewLifecycleOwner, { sign_up_error ->
            when (sign_up_error.first) {
                EMAIL_CHECK_ERROR -> editTextEmail
                PASSWORD_CHECK_ERROR -> editTextPassword
                else -> null
            }?.apply {
                error = sign_up_error.second
                requestFocus()
            }
            when (sign_up_error.first) {
                SERVER_SIGN_UP_ERROR ->
                    Toast.makeText(context, sign_up_error.second, Toast.LENGTH_SHORT).show()
            }
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
}