package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.custom.CustomEditTextView
import com.ilyasov.sci_king.presentation.adapters.SciArticleAdapter
import com.ilyasov.sci_king.presentation.view_models.SciArticlesViewModel
import com.ilyasov.sci_king.util.Constants.Companion.BASE_KEYWORD
import com.ilyasov.sci_king.util.isVisible
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var customSearchView: CustomEditTextView
    private var keyWord: String = BASE_KEYWORD
    private val viewModel by lazy { SciArticlesViewModel(requireActivity().application) }
    private val sciArticlesAdapter: SciArticleAdapter by lazy { SciArticleAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        customSearchView = view.findViewById(R.id.search_by_title)

        search_by_title.setActionOnClick {
            view.findViewById<AppCompatImageView>(R.id.search_by_title_img).setOnClickListener {
                keyWord = if (customSearchView.text == "") keyWord else customSearchView.text
                viewModel.getSciArticlesByKeyWord(keyWord)
                customSearchView.showError()
            }
        }

        viewModel.sciArticlesListLiveData.observe(viewLifecycleOwner, { response ->
            sciArticlesAdapter.listOfItems = response
            sciArticlesAdapter.notifyDataSetChanged()
        })

        viewModel.errorStateLiveData.observe(viewLifecycleOwner, { error ->
            Log.e("TAG", "An error: $error")
        })

        viewModel.loadingMutableLiveData.observe(viewLifecycleOwner, { visibility ->
            progress_bar.isVisible(visibility)
        })

        sciArticlesAdapter.userSavedArticles.observe(viewLifecycleOwner, { sciArticle ->
            viewModel.insert(sciArticle)
        })

        swipe_refresh.setOnRefreshListener {
            viewModel.getSciArticlesByKeyWord(keyWord)
            swipe_refresh.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSciArticlesByKeyWord(keyWord)
    }

    private fun setupRecyclerView() {
        rv_sci_articles.apply {
            adapter = sciArticlesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}