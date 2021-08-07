package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.presentation.adapters.SciArticleAdapter
import com.ilyasov.sci_king.presentation.view_models.SavedArticlesViewModel
import com.ilyasov.sci_king.util.isVisible
import kotlinx.android.synthetic.main.fragment_saved_articles.*

class SavedArticlesFragment : Fragment(R.layout.fragment_saved_articles) {
    private val viewModel by lazy { SavedArticlesViewModel(requireActivity().application) }
    private val sciArticlesAdapter: SciArticleAdapter by lazy { SciArticleAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        viewModel.sciArticlesListLiveData.observe(viewLifecycleOwner, { response ->
            sciArticlesAdapter.listOfItems = response
            sciArticlesAdapter.notifyDataSetChanged()
        })

        viewModel.loadingMutableLiveData.observe(viewLifecycleOwner, { visibility ->
            progress_bar.isVisible(visibility)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSavedArticles()
    }

    private fun setupRecyclerView() {
        rv_sci_articles.apply {
            adapter = sciArticlesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}