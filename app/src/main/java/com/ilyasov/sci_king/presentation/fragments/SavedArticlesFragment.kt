package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.presentation.adapters.SciArticleAdapter
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.presentation.viewmodels.SavedArticlesViewModel
import com.ilyasov.sci_king.util.Constants.Companion.SCI_ARTICLE_TO_READ
import com.ilyasov.sci_king.util.action
import com.ilyasov.sci_king.util.isVisible
import com.ilyasov.sci_king.util.longSnackBar
import kotlinx.android.synthetic.main.fragment_saved_articles.*

class SavedArticlesFragment : BaseFragment(R.layout.fragment_saved_articles) {
    private val viewModel: SavedArticlesViewModel by lazy { createViewModel {} }
    private lateinit var sciArticlesAdapter: SciArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sciArticlesAdapter =
            SciArticleAdapter(isAnonymous = viewModel.getCurrentUser() == null,
                customBoolean = true) { sci_article ->
                onClickArticle(sci_article)
            }
        setupRecyclerView()
        setupObserver()
        setupSwipeToDeleteFunction()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSavedArticles()
    }

    private fun onClickArticle(sciArticle: SciArticle) {
        sharedPrefs.edit().apply {
            putString(SCI_ARTICLE_TO_READ, gson.toJson(sciArticle)); apply()
        }
        Navigation.findNavController(requireActivity(), R.id.navHostFragmentActivityRoot)
            .navigate(R.id.action__MainFragment__to__ReadArticle_Flow)
    }

    private fun setupRecyclerView() {
        rvSavedArticles.apply {
            adapter = sciArticlesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setupSwipeToDeleteFunction() {
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                sciArticlesAdapter.listOfItems[viewHolder.adapterPosition].let { article ->
                    val job = viewModel.removeSciArticleFromLocalDB(article, 2000L) {
                        sciArticlesAdapter.listOfItems.removeAt(viewHolder.adapterPosition)
                        sciArticlesAdapter.notifyDataSetChanged()
                    }
                    requireView().longSnackBar(R.string.movie_deleted) {
                        action(R.string.undo) {
                            job.cancel()
                            sciArticlesAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(rvSavedArticles)
    }

    private fun setupObserver() {
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
        sciArticlesAdapter.onClickCloudDownloadLiveData.observe(viewLifecycleOwner) { sciArticle ->
            viewModel.uploadToCloud(sciArticle)
        }
    }
}