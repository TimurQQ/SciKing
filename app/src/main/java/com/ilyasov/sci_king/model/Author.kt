package com.ilyasov.sci_king.model

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "author")
data class Author(
    @PropertyElement val name : String,
    @PropertyElement val affiliation : String?
)