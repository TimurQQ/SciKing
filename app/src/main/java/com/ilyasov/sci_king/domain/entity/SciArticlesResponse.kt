package com.ilyasov.sci_king.domain.entity

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "feed")
data class SciArticlesResponse(
    @PropertyElement(name = "link") val link: String,
    @PropertyElement(name = "title") val title: String,
    @PropertyElement(name = "id") val id: String,
    @PropertyElement(name = "updated") val updateTime: String,
    @PropertyElement(name = "opensearch:totalResults") val totalResults: String,
    @PropertyElement(name = "opensearch:startIndex") val startIndex: String,
    @PropertyElement(name = "opensearch:itemsPerPage") val itemPerPage: String,
    @Element(name = "entry") val articles: MutableList<SciArticle>
)
