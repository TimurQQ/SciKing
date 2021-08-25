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
import com.ilyasov.sci_king.util.isVisible
import kotlinx.android.synthetic.main.fragment_saved_articles.*

class SavedArticlesFragment : BaseFragment(R.layout.fragment_saved_articles) {
    private val viewModel: SavedArticlesViewModel by lazy { createViewModel {} }
    private lateinit var sciArticlesAdapter: SciArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sciArticlesAdapter =
            SciArticleAdapter(
                isAnonim = viewModel.getCurrentUser() == null,
                customBoolean = false) { sci_article ->
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
        viewModel.callbackLiveData.observe(viewLifecycleOwner) { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.navigationStateLiveData.observe(viewLifecycleOwner) { navHostFragmentId ->
            Navigation.findNavController(
                requireActivity(),
                navHostFragmentId
            ).navigate(R.id.action__MainFragment__to__ReadArticle_Flow)
        }
        sciArticlesAdapter.onClickCloudDownloadLiveData.observe(viewLifecycleOwner) { sciArticle ->
            viewModel.uploadToCloud(sciArticle)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSavedArticles()
    }

    private fun onClickArticle(sciArticle: SciArticle) {
        viewModel.startReadingArticle(sciArticle)
    }

    private fun setupRecyclerView() {
        rvSavedArticles.apply {
            adapter = sciArticlesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}