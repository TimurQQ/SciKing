package com.ilyasov.sci_king.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList

@Element(name = "entry")
data class SciArticle(
    @Element(name = "id") val id : Int,
    @Element(name = "updated") val timeUpdated : String,
    @Element(name = "published") val timePublished : String,
    @Element(name = "title") val title : String,
    @Element(name = "summary") val summary : String,
    @ElementList val authors : List<Author>,
    @Element val doi : String,
    @Element val citationsLink : String,
    @Element val comment : String,
    @Element val journalRef : String,
    @Element val articleLink : String,
    @Element val pdfLink : String
)