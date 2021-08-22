package com.ilyasov.sci_king.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.custom.CustomEditTextView
import com.ilyasov.sci_king.presentation.adapters.SciArticleAdapter
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.presentation.viewmodels.SciArticlesViewModel
import com.ilyasov.sci_king.util.Constants.Companion.APP_PREFERENCES
import com.ilyasov.sci_king.util.Constants.Companion.BASE_KEYWORD
import com.ilyasov.sci_king.util.Constants.Companion.SCI_ARTICLE_TO_READ
import com.ilyasov.sci_king.util.isVisible
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(R.layout.fragment_search) {
    private lateinit var customSearchView: CustomEditTextView
    private var keyWord: String = BASE_KEYWORD
    private var builder = GsonBuilder().setPrettyPrinting()
    private var gson = builder.create()
    private val viewModel: SciArticlesViewModel by lazy {
        createViewModel {}
    }
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var sciArticlesAdapter: SciArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefs = requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        sciArticlesAdapter = SciArticleAdapter({ sci_article ->
            sharedPrefs.edit().apply {
                putString(SCI_ARTICLE_TO_READ, gson.toJson(sci_article))
                apply()
            }
            Navigation.findNavController(
                requireActivity(),
                R.id.activity_root__fragment__nav_host
            ).navigate(R.id.action__MainFragment__to__ReadArticle_Flow)
        }, true)
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
            val len = sciArticlesAdapter.itemCount
            synchronized(response) {
                for (i in 0 until len) {
                    viewModel.isArticleSaved(response[i]) { isSaved ->
                        sciArticlesAdapter.itemStateArray.append(i, isSaved)
                    }
                }
            }
            sciArticlesAdapter.notifyDataSetChanged()
            changeVisibilities()
        })

        viewModel.errorStateLiveData.observe(viewLifecycleOwner, { error ->
            Log.e("TAG", "An error: $error")
        })

        viewModel.loadingMutableLiveData.observe(viewLifecycleOwner, { visibility ->
            progress_bar.isVisible(visibility)
        })

        swipe_refresh.setOnRefreshListener {
            viewModel.getSciArticlesByKeyWord(keyWord)
            swipe_refresh.isRefreshing = false
        }

        sciArticlesAdapter.userSavedArticles.observe(viewLifecycleOwner, { sciArticle_andCallback ->
            viewModel.insert(sciArticle_andCallback.first, sciArticle_andCallback.second)
        })

        sciArticlesAdapter.onClickDownloadLiveData.observe(viewLifecycleOwner, { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        })
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

    private fun changeVisibilities() {
        val len: Int = sciArticlesAdapter.itemCount
        for (i in 0 until len) {
            changeVisibility(i)
        }
    }

    private fun changeVisibility(itemIndex: Int) {
        viewModel.isArticleSaved(sciArticlesAdapter.listOfItems[itemIndex]) { isSaved ->
            sciArticlesAdapter.notifyItemRangeChanged(itemIndex, 1, !isSaved)
        }
    }
}