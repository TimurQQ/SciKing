package com.ilyasov.sci_king.presentation.fragments

import android.os.Bundle
import android.view.View
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.presentation.viewmodels.ParseArticleViewModel
import com.ilyasov.sci_king.presentation.viewmodels.ParseArticleViewModel.Companion.file
import com.ilyasov.sci_king.util.*
import com.ilyasov.sci_king.util.Constants.Companion.SCI_ARTICLE_TO_READ
import kotlinx.android.synthetic.main.fragment_book_read.*

//import org.allenai.scienceparse.Parser
//import org.allenai.scienceparse.ExtractedMetadata

class ParseArticleFragment : BaseFragment(R.layout.fragment_book_read) {
    //val parser: Parser = Parser.getInstance()
    private val readBookViewModel: ParseArticleViewModel by lazy { createViewModel {} }
    private lateinit var restoredObject: SciArticle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val restoredObjectDataString =
            sharedPrefs.getString(SCI_ARTICLE_TO_READ, "null").toString()
        restoredObject = gson.fromJson(restoredObjectDataString, SciArticle::class.java)
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

        // Show user download progress
        StaticVariables.downloadingItems.observe(viewLifecycleOwner, { item ->
            item[restoredObject.id]?.let {
                val totalPercent = it.calculateProgress()
                progressBarDownloadState.progress = totalPercent
                tvProcessPercent.text = "$totalPercent".plus(" %")
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
        // If book currently downloading than show progressbar
        if (StaticVariables.downloadingItems.value?.get(restoredObject.id) == null) {
            val newLink = "http://export.${
                restoredObject.links[restoredObject.links.size - 1].toString().split("//")[1]
            }"
            readBookViewModel.getBookData(
                newLink,
                restoredObject.id,
                requireContext()
            )
        }
    }
}