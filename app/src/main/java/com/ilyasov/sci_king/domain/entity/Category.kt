package com.ilyasov.sci_king.domain.entity

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Category(
    @Attribute(name = "term") val name: String
)
