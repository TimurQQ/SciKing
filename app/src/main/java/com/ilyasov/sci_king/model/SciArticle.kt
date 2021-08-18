package com.ilyasov.sci_king.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Entity
@Xml(name = "entry")
data class SciArticle(
    @SerializedName("article_id")
    @PrimaryKey @PropertyElement(name = "id") val id: String,
    @SerializedName("time_updated")
    @PropertyElement(name = "updated") val timeUpdated: String,
    @SerializedName("time_published")
    @PropertyElement(name = "published") val timePublished: String,
    @SerializedName("article_title")
    @PropertyElement(name = "title") val title: String,
    @SerializedName("summary")
    @PropertyElement(name = "summary") val summary: String,
    @SerializedName("authors")
    @Element(name = "author") val authors: List<Author>,
    @SerializedName("doi")
    @PropertyElement(name = "arxiv:doi") val doi: String?,
    @SerializedName("links")
    @Element(name = "link") val links: List<Link>,
    @SerializedName("article_comment")
    @PropertyElement(name = "arxiv:comment") val comment: String?,
    @SerializedName("journal_reference")
    @PropertyElement(name = "arxiv:journal_ref") val journalRef: String?,
    @SerializedName("primary_category")
    @Element(name = "arxiv:primary_category") val primaryCategory: Category
)