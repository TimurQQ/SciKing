package com.ilyasov.sci_king.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "feed", strict = false)
data class SciArticlesResponse(
    @Element(name = "link") val link : String,
    @Element(name = "title") val title: String,
    @Element(name = "id") val id : Int,
    @Element(name = "updated") val updateTime : String,
    @Element(name = "opensearch:totalResults") val totalResults : Int,
    @Element(name = "opensearch:startIndex") val startIndex : Int,
    @Element(name = "opensearch:itemsPerPage") val itemPerPage : Int,
    @ElementList val articles: List<SciArticle>
)
