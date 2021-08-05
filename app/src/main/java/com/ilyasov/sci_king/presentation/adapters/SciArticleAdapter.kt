package com.ilyasov.sci_king.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.model.SciArticle
import kotlinx.android.synthetic.main.sci_article_item.view.*

class SciArticleAdapter : RecyclerView.Adapter<SciArticleAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var listOfItems: List<SciArticle> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ArticleViewHolder(
        LayoutInflater.from(
            parent.context
        ).inflate(R.layout.sci_article_item, parent, false)
    )

    override fun getItemCount() = listOfItems.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val sciArticle = listOfItems[position]
        holder.itemView.apply {
            tv_title.text = sciArticle.title
            tv_description.text = sciArticle.summary
        }
    }
}