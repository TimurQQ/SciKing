package com.ilyasov.sci_king.presentation.adapters

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.gson.GsonBuilder
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.model.SciArticle
import com.ilyasov.sci_king.util.isVisible
import com.ilyasov.sci_king.util.makeInvisible
import kotlinx.android.synthetic.main.sci_article_item.view.*

class SciArticleAdapter(
    private val onClick: (article: SciArticle) -> Unit,
    private val customBoolean: Boolean
) :
    RecyclerView.Adapter<SciArticleAdapter.ArticleViewHolder>() {
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val itemStateArray = SparseBooleanArray()
    val userSavedArticles: MutableLiveData<Pair<SciArticle, () -> Unit>> = MutableLiveData()
    val onClickDownloadLiveData: MutableLiveData<String> = MutableLiveData()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private var builder = GsonBuilder().setPrettyPrinting()
    private var gson = builder.create()

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val downloadButton: AppCompatImageButton by lazy {
            itemView.findViewById(R.id.imageView)
        }
        val cloudDownloadButton: AppCompatImageButton by lazy {
            itemView.findViewById(R.id.imageCloudView)
        }

        fun bind(position: Int) {
            if (itemStateArray[position, false]) {
                downloadButton.makeInvisible()
            } else {
                downloadButton.isVisible(customBoolean)
            }
        }
    }

    var listOfItems: List<SciArticle> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView = LayoutInflater.from(
            parent.context
        ).inflate(R.layout.sci_article_item, parent, false)
        return ArticleViewHolder(itemView)
    }

    override fun getItemCount() = listOfItems.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val sciArticle = listOfItems[position]
        holder.itemView.apply {
            tv_title.text = sciArticle.title
            tv_description.text = sciArticle.summary
            setOnClickListener {
                onClick.invoke(sciArticle)
            }
        }
        holder.downloadButton.setOnClickListener {
            userSavedArticles.postValue(Pair(sciArticle, {
                holder.downloadButton.makeInvisible()
            }))
        }
        if (mAuth.currentUser == null) {
            holder.cloudDownloadButton.makeInvisible()
        }
        holder.cloudDownloadButton.setOnClickListener {
            val json: String = gson.toJson(sciArticle)
            val textRef = storageRef.child("/${mAuth.currentUser!!.uid}/${sciArticle.id}.txt")
            val data = json.toByteArray()
            val uploadTask: UploadTask = textRef.putBytes(data)
            uploadTask.addOnFailureListener {
                onClickDownloadLiveData.postValue("failure :(")
            }.addOnSuccessListener {
                onClickDownloadLiveData.postValue("success :)")
            }
        }
        holder.bind(position)
    }

    override fun onBindViewHolder(
        viewHolder: ArticleViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            // Perform a full update
            onBindViewHolder(viewHolder, position)
        } else {
            // Perform a partial update
            for (payload in payloads) {
                if (payload is Boolean) {
                    viewHolder.downloadButton.isVisible(payload)
                }
            }
        }
    }
}