package com.ilyasov.sci_king.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.presentation.adapters.SciArticleAdapter
import com.ilyasov.sci_king.presentation.view_models.SavedArticlesViewModel
import com.ilyasov.sci_king.util.Constants
import com.ilyasov.sci_king.util.isVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_saved_articles.*

@AndroidEntryPoint
class SavedArticlesFragment : Fragment(R.layout.fragment_saved_articles) {
    private val viewModel by lazy { SavedArticlesViewModel(requireActivity().application) }
    private var builder = GsonBuilder().setPrettyPrinting()
    private var gson = builder.create()
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var sciArticlesAdapter: SciArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefs = requireContext().getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
        sciArticlesAdapter = SciArticleAdapter ({ sci_article ->
            sharedPrefs.edit().apply {
                putString("SCI_ARTICLE_TO_READ", gson.toJson(sci_article))
                apply()
            }
            Navigation.findNavController(
                requireActivity(),
                R.id.activity_root__fragment__nav_host
            ).navigate(R.id.action__MainFragment__to__ReadArticle_Flow)
        }, false)
        setupRecyclerView()

        viewModel.sciArticlesListLiveData.observe(viewLifecycleOwner, { response ->
            sciArticlesAdapter.listOfItems = response
            sciArticlesAdapter.notifyDataSetChanged()
        })

        viewModel.loadingMutableLiveData.observe(viewLifecycleOwner, { visibility ->
            progress_bar.isVisible(visibility)
        })

        sciArticlesAdapter.onClickDownloadLiveData.observe(viewLifecycleOwner, { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
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