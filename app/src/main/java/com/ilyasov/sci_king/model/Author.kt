package com.ilyasov.sci_king.model

import org.simpleframework.xml.Element

@Element(name = "author")
data class Author(
    @Element val name : String,
    @Element val affiliation : String
)