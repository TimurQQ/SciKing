package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.View
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.presentation.viewmodels.ParseArticleViewModel
import com.ilyasov.sci_king.presentation.viewmodels.ParseArticleViewModel.Companion.file
import com.ilyasov.sci_king.util.*
import com.ilyasov.sci_king.util.Constants.Companion.PERCENT_SIGN
import kotlinx.android.synthetic.main.fragment_book_read.*

class ParseArticleFragment : BaseFragment(R.layout.fragment_book_read) {
    private val viewModel: ParseArticleViewModel by lazy { createViewModel {} }
    private lateinit var restoredObject: SciArticle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoredObject = viewModel.getCurrentBook()
    }

    private fun setupObserver() {
        file.observe(viewLifecycleOwner, {
            changeLayoutVisibilities()
            pdfView.fromUri(it)
                .pageFling(true)
                .swipeHorizontal(true)
                .pageSnap(true)
                .load()
        })

        StaticVariables.downloadingItems.observe(viewLifecycleOwner, { item ->
            item[restoredObject.id]?.let {
                val totalPercent = it.calculateProgress()
                progressBarDownloadState.progress = totalPercent
                tvProcessPercent.text = "$totalPercent".plus(PERCENT_SIGN)
                tvProcessPercentText.text = it.calculateProgressString()
            }
        })
    }

    private fun changeLayoutVisibilities() {
        loadingViewRead.makeInvisible()
        containerViewRead.makeVisible()
    }

    override fun onStart() {
        super.onStart()
        setupObserver()
        if (StaticVariables.downloadingItems.value?.get(restoredObject.id) == null) {
            val newLink = "http://export.${
                restoredObject.links[restoredObject.links.size - 1].toString().split("//")[1]
            }"
            viewModel.getBookData(newLink, restoredObject.id, requireContext())
        }
    }
}