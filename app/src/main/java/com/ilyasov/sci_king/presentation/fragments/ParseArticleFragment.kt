package com.ilyasov.sci_king.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eneskayiklik.comicreader.ui.main.read.ParseArticleViewModel
import com.eneskayiklik.comicreader.ui.main.read.ParseArticleViewModel.Companion.file
import com.google.gson.GsonBuilder
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.model.SciArticle
import com.ilyasov.sci_king.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_book_read.*
//import org.allenai.scienceparse.Parser
//import org.allenai.scienceparse.ExtractedMetadata

@AndroidEntryPoint
class ParseArticleFragment : Fragment(R.layout.fragment_book_read) {
    //val parser: Parser = Parser.getInstance()
    private lateinit var sharedPrefs: SharedPreferences
    private val readBookViewModel: ParseArticleViewModel by viewModels()
    private var builder = GsonBuilder().setPrettyPrinting()
    private var gson = builder.create()
    private lateinit var restoredObjectDataString: String
    private lateinit var restoredObject: SciArticle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefs =
            requireContext().getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
        restoredObjectDataString = sharedPrefs.getString("SCI_ARTICLE_TO_READ", "null").toString()
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