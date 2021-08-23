package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.presentation.adapters.SciArticleAdapter
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.presentation.viewmodels.SavedArticlesViewModel
import com.ilyasov.sci_king.util.Constants.Companion.SCI_ARTICLE_TO_READ
import com.ilyasov.sci_king.util.isVisible
import kotlinx.android.synthetic.main.fragment_saved_articles.*

class SavedArticlesFragment : BaseFragment(R.layout.fragment_saved_articles) {
    private val viewModel: SavedArticlesViewModel by lazy { createViewModel {} }
    private lateinit var sciArticlesAdapter: SciArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sciArticlesAdapter = SciArticleAdapter(false) { sci_article ->
            onClickArticle(sci_article)
        }
        setupRecyclerView()
        viewModel.sciArticlesListLiveData.observe(viewLifecycleOwner, { response ->
            sciArticlesAdapter.listOfItems = response
            sciArticlesAdapter.notifyDataSetChanged()
        })
        viewModel.loadingMutableLiveData.observe(viewLifecycleOwner, { visibility ->
            progressBar.isVisible(visibility)
        })
        sciArticlesAdapter.onClickDownloadLiveData.observe(viewLifecycleOwner, { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSavedArticles()
    }

    private fun onClickArticle(sci_article: SciArticle) {
        sharedPrefs.edit().apply {
            putString(SCI_ARTICLE_TO_READ, gson.toJson(sci_article))
            apply()
        }
        Navigation.findNavController(
            requireActivity(),
            R.id.navHostFragmentActivityRoot
        ).navigate(R.id.action__MainFragment__to__ReadArticle_Flow)
    }

    private fun setupRecyclerView() {
        rvSavedArticles.apply {
            adapter = sciArticlesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}