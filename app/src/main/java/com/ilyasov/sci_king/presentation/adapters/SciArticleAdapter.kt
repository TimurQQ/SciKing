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
import com.google.gson.Gson
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.SciKingApplication
import com.ilyasov.sci_king.domain.entity.SciArticle
import com.ilyasov.sci_king.util.Constants.Companion.FAILURE_MSG
import com.ilyasov.sci_king.util.Constants.Companion.SUCCESS_MSG
import com.ilyasov.sci_king.util.isVisible
import com.ilyasov.sci_king.util.makeInvisible
import kotlinx.android.synthetic.main.sci_article_item.view.*
import javax.inject.Inject

class SciArticleAdapter(
    private val customBoolean: Boolean = true,
    private val onClick: (article: SciArticle) -> Unit = {}
) :
    RecyclerView.Adapter<SciArticleAdapter.ArticleViewHolder>() {
    @Inject
    lateinit var gson: Gson
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val storageRef = FirebaseStorage.getInstance().reference
    val itemStateArray = SparseBooleanArray()
    val userSavedArticles: MutableLiveData<Pair<SciArticle, () -> Unit>> = MutableLiveData()
    val onClickDownloadLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        SciKingApplication.appComponent.inject(this)
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val downloadButton: AppCompatImageButton by lazy {
            itemView.findViewById(R.id.btnLocalSave)
        }
        val cloudDownloadButton: AppCompatImageButton by lazy {
            itemView.findViewById(R.id.btnCloudSave)
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
            tvArticleTitle.text = sciArticle.title
            tvArticleDescription.text = sciArticle.summary
            setOnClickListener { onClick.invoke(sciArticle) }
        }
        holder.downloadButton.setOnClickListener {
            userSavedArticles.postValue(Pair(sciArticle) { holder.downloadButton.makeInvisible() })
        }
        if (mAuth.currentUser == null) {
            holder.cloudDownloadButton.makeInvisible()
        }
        holder.cloudDownloadButton.setOnClickListener {
            val textRef = storageRef.child("/${mAuth.currentUser!!.uid}/${sciArticle.id}.txt")
            val data = gson.toJson(sciArticle).toByteArray()
            val uploadTask: UploadTask = textRef.putBytes(data)
            uploadTask.addOnFailureListener {
                onClickDownloadLiveData.postValue(FAILURE_MSG)
            }.addOnSuccessListener {
                onClickDownloadLiveData.postValue(SUCCESS_MSG)
            }
        }
        holder.bind(position)
    }

    override fun onBindViewHolder(
        viewHolder: ArticleViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(viewHolder, position)
        } else {
            for (payload in payloads) {
                if (payload is Boolean) {
                    viewHolder.downloadButton.isVisible(payload)
                }
            }
        }
    }
}