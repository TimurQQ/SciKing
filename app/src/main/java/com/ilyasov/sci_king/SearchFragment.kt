package com.ilyasov.sci_king

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SearchFragment : Fragment() {

    private val viewModel by lazy {
        SciArticlesViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onResume() {
        super.onResume()
        viewModel.getSciArticlesByKeyWord("electron") //TODO use constants
    }
}