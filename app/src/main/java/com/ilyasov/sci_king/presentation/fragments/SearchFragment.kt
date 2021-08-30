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
import com.ilyasov.sci_king.util.showToast
import kotlinx.android.synthetic.main.custom_edit_text_view.*
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(R.layout.fragment_search) {
    private var keyWord: String = BASE_KEYWORD
    private val viewModel: SciArticlesViewModel by lazy { createViewModel {} }
    private lateinit var sciArticlesAdapter: SciArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sciArticlesAdapter =
            SciArticleAdapter(isAnonymous = viewModel.getCurrentUser() == null,
                customBoolean = false) { sci_article ->
                onClickArticle(sci_article)
            }
        setupRecyclerView()
        setupObserver()
        edtSearchByKeyword.setActionOnClick {
            imgCustomView.setOnClickListener { onClickImgButtonSearch() }
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

    private fun setupObserver() {
        viewModel.errorStateLiveData.observe(viewLifecycleOwner) { error ->
            showToast("An error: $error")
        }
        viewModel.loadingMutableLiveData.observe(viewLifecycleOwner) { visibility ->
            progressBar.isVisible(visibility)
        }
        viewModel.callbackLiveData.observe(viewLifecycleOwner) { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getSciArticlesByKeyWord(keyWord)
            swipeRefreshLayout.isRefreshing = false
        }
        setupBookMarkCallback()
        sciArticlesAdapter.onClickCloudDownloadLiveData.observe(viewLifecycleOwner) { sciArticle ->
            viewModel.uploadToCloud(sciArticle)
        }
        viewModel.sciArticlesListLiveData.observe(viewLifecycleOwner) { response ->
            sciArticlesAdapter.listOfItems = response
            fillItemStateArray(response)
            sciArticlesAdapter.notifyDataSetChanged()
            changeVisibilities(sciArticlesAdapter.itemCount)
        }
    }

    private fun fillItemStateArray(items: List<SciArticle>) {
        for (i in 0 until sciArticlesAdapter.itemCount) {
            viewModel.isArticleSaved(items[i]) { isSaved ->
                sciArticlesAdapter.itemStateArray.append(i, isSaved)
            }
        }
    }

    private fun setupBookMarkCallback() {
        sciArticlesAdapter.userSavedArticlesLiveData.observe(viewLifecycleOwner) { sciArticlePair_andCallback ->
            viewModel.isArticleSaved(sciArticlePair_andCallback.first.first) { isSaved ->
                val func: SearchFragment.(SciArticle, Int, (Boolean) -> Unit) -> Unit =
                    if (isSaved) SearchFragment::removeSciArticle else SearchFragment::addSciArticle
                func(sciArticlePair_andCallback.first.first,
                    sciArticlePair_andCallback.first.second,
                    sciArticlePair_andCallback.second)
            }
        }
    }

    private fun removeSciArticle(
        sciArticle: SciArticle, position: Int, callback: (Boolean) -> Unit,
    ) {
        viewModel.removeSciArticleFromLocalDB(sciArticle, callback) {
            sciArticlesAdapter.itemStateArray.append(position, false)
            sciArticlesAdapter.notifyDataSetChanged()
            changeVisibilities(sciArticlesAdapter.itemCount)
        }
    }

    private fun addSciArticle(
        sciArticle: SciArticle, position: Int, callback: (Boolean) -> Unit,
    ) {
        viewModel.addSciArticleToLocalDB(sciArticle, callback) {
            sciArticlesAdapter.itemStateArray.append(position, true)
            sciArticlesAdapter.notifyDataSetChanged()
            changeVisibilities(sciArticlesAdapter.itemCount)
        }
    }

    private fun onClickImgButtonSearch() {
        keyWord = if (edtSearchByKeyword.text == "") keyWord else edtSearchByKeyword.text
        viewModel.getSciArticlesByKeyWord(keyWord)
        edtSearchByKeyword.showError()
    }

    private fun onClickArticle(sciArticle: SciArticle) {
        sharedPrefs.edit().apply {
            putString(SCI_ARTICLE_TO_READ, gson.toJson(sciArticle)); apply()
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
            sciArticlesAdapter.notifyItemRangeChanged(itemIndex, 1, isSaved)
        }
    }
}