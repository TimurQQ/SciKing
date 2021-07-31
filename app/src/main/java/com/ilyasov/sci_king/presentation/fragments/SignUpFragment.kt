package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.presentation.view_models.SignUpViewModel

class SignUpFragment : Fragment() {
    private val viewModel: SignUpViewModel by lazy {
        SignUpViewModel(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated(view)
    }
}