package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.presentation.adapters.SciArticleAdapter
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.presentation.viewmodels.SciArticlesViewModel
import com.ilyasov.sci_king.util.Constants.Companion.BASE_KEYWORD
import com.ilyasov.sci_king.util.Constants.Companion.SCI_ARTICLE_TO_READ
import com.ilyasov.sci_king.util.isVisible
import kotlinx.android.synthetic.main.custom_edit_text_view.*
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(R.layout.fragment_search) {
    private var keyWord: String = BASE_KEYWORD
    private val viewModel: SciArticlesViewModel by lazy { createViewModel {} }
    private lateinit var sciArticlesAdapter: SciArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sciArticlesAdapter = SciArticleAdapter(true) { sci_article ->
            onClickArticle(sci_article)
        }
        setupRecyclerView()
        edtSearchByKeyword.setActionOnClick {
            imgCustomView.setOnClickListener { onClickImgButtonSearch() }
        }
        viewModel.sciArticlesListLiveData.observe(viewLifecycleOwner) { response ->
            sciArticlesAdapter.listOfItems = response
            val len = sciArticlesAdapter.itemCount
            for (i in 0 until len) {
                viewModel.isArticleSaved(response[i]) { isSaved ->
                    sciArticlesAdapter.itemStateArray.append(i, isSaved)
                }
            }
            sciArticlesAdapter.notifyDataSetChanged()
            changeVisibilities(len)
        }
        viewModel.errorStateLiveData.observe(viewLifecycleOwner) { error ->
            Log.e("TAG", "An error: $error")
        }
        viewModel.loadingMutableLiveData.observe(viewLifecycleOwner) { visibility ->
            progressBar.isVisible(visibility)
        }
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getSciArticlesByKeyWord(keyWord)
            swipeRefreshLayout.isRefreshing = false
        }
        sciArticlesAdapter.userSavedArticles.observe(viewLifecycleOwner) { sciArticle_andCallback ->
            viewModel.addSciArticleToLocalDB(
                sciArticle_andCallback.first,
                sciArticle_andCallback.second
            )
        }
        sciArticlesAdapter.onClickDownloadLiveData.observe(viewLifecycleOwner) { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSciArticlesByKeyWord(keyWord)
    }

    private fun setupRecyclerView() {
        rvFoundSciArticles.apply {
            adapter = sciArticlesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun onClickImgButtonSearch() {
        keyWord = if (edtSearchByKeyword.text == "") keyWord else edtSearchByKeyword.text
        viewModel.getSciArticlesByKeyWord(keyWord)
        edtSearchByKeyword.showError()
    }

    private fun onClickArticle(sci_article: SciArticle) {
        sharedPrefs.edit().apply {
            putString(SCI_ARTICLE_TO_READ, gson.toJson(sci_article)); apply()
        }
        Navigation.findNavController(requireActivity(), R.id.navHostFragmentActivityRoot)
            .navigate(R.id.action__MainFragment__to__ReadArticle_Flow)
    }

    private fun changeVisibilities(count: Int) {
        for (i in 0 until count) {
            changeVisibility(i)
        }
    }

    private fun changeVisibility(itemIndex: Int) {
        viewModel.isArticleSaved(sciArticlesAdapter.listOfItems[itemIndex]) { isSaved ->
            sciArticlesAdapter.notifyItemRangeChanged(itemIndex, 1, !isSaved)
        }
    }
}