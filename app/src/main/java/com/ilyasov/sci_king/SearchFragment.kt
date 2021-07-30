package com.ilyasov.sci_king

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ilyasov.sci_king.custom.CustomEditTextView
import com.ilyasov.sci_king.presentation.adapters.SciArticleAdapter
import com.ilyasov.sci_king.util.Constants.Companion.BASE_KEYWORD
import com.ilyasov.sci_king.util.isVisible
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var customSearchView: CustomEditTextView
    private var keyWord : String = BASE_KEYWORD
    private val viewModel by lazy {
        SciArticlesViewModel()
    }
    private val sciArticlesAdapter: SciArticleAdapter by lazy {
        SciArticleAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        customSearchView = view.findViewById(R.id.search_by_title)
        customSearchView.setActionOnClick {
            view.findViewById<AppCompatImageView>(R.id.search_by_title_img).setOnClickListener {
                keyWord = customSearchView.text
                viewModel.getSciArticlesByKeyWord(keyWord)
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

        swipeRefresh = view.findViewById(R.id.swipe_refresh)
        swipeRefresh.setOnRefreshListener {
            viewModel.getSciArticlesByKeyWord(keyWord)
            swipeRefresh.isRefreshing = false
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