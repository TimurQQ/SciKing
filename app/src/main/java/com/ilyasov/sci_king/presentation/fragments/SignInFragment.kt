package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.presentation.view_models.SignInViewModel

class SignInFragment : Fragment() {
    private val viewModel: SignInViewModel by lazy {
        SignInViewModel(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated(view)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
}