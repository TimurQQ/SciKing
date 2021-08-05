package com.ilyasov.sci_king.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "entry")
data class SciArticle(
    @PropertyElement(name = "id") val id: String,
    @PropertyElement(name = "updated") val timeUpdated: String,
    @PropertyElement(name = "published") val timePublished: String,
    @PropertyElement(name = "title") val title: String,
    @PropertyElement(name = "summary") val summary: String,
    @Element(name = "author") val authors: List<Author>,
    @PropertyElement(name = "arxiv:doi") val doi: String?,
    @Element(name = "link") val links: List<Link>,
    @PropertyElement(name = "arxiv:comment") val comment: String?,
    @PropertyElement(name = "arxiv:journal_ref") val journalRef: String?,
    @Element(name = "arxiv:primary_category") val primaryCategory: Category
)