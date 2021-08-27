package com.ilyasov.sci_king.presentation.adapters

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.util.isVisible
import com.ilyasov.sci_king.util.makeInvisible
import com.ilyasov.sci_king.util.setBookMarkSource
import kotlinx.android.synthetic.main.sci_article_item.view.*

class SciArticleAdapter(
    private val isAnonim: Boolean = true,
    private val customBoolean: Boolean = true,
    private val onClick: (article: SciArticle) -> Unit = {},
) :
    RecyclerView.Adapter<SciArticleAdapter.ArticleViewHolder>() {
    val itemStateArray = SparseBooleanArray()
    val userSavedArticlesLiveData: MutableLiveData<Pair<Pair<SciArticle, Int>, (Boolean) -> Unit>> = MutableLiveData()
    val onClickCloudDownloadLiveData: MutableLiveData<SciArticle> = MutableLiveData()

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val downloadButton: AppCompatImageButton by lazy {
            itemView.findViewById(R.id.btnLocalSave)
        }
        val cloudDownloadButton: AppCompatImageButton by lazy {
            itemView.findViewById(R.id.btnCloudSave)
        }

        fun bind(position: Int) {
            if (itemStateArray[position, false]) {
                downloadButton.setImageResource(R.drawable.ic_bookmark)
            } else {
                downloadButton.setBookMarkSource(customBoolean)
            }
        }
    }

    var listOfItems: MutableList<SciArticle> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ArticleViewHolder(
        LayoutInflater.from(
            parent.context
        ).inflate(R.layout.sci_article_item, parent, false)
    )

    override fun getItemCount() = listOfItems.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val sciArticle = listOfItems[position]
        holder.apply {
            itemView.apply {
                tvArticleTitle.text = sciArticle.title
                tvArticleDescription.text = sciArticle.summary
                setOnClickListener { onClick.invoke(sciArticle) }
            }
            downloadButton.setOnClickListener {
                userSavedArticlesLiveData.postValue(Pair(Pair(sciArticle, position)) { isSaved ->
                    holder.downloadButton.setBookMarkSource(isSaved)
                })
            }
            cloudDownloadButton.apply {
                isVisible(!isAnonim)
                setOnClickListener { onClickCloudDownloadLiveData.postValue(sciArticle) }
            }
            bind(position)
        }
    }

    override fun onBindViewHolder(
        viewHolder: ArticleViewHolder, position: Int, payloads: MutableList<Any>,
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(viewHolder, position)
        } else {
            for (payload in payloads) {
                if (payload is Boolean) {
                    viewHolder.downloadButton.setBookMarkSource(payload)
                }
            }
        }
    }
}