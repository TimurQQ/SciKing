package com.ilyasov.sci_king.domain.entity

import com.google.gson.annotations.SerializedName
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "author")
data class Author(
    @SerializedName("name") @PropertyElement val name: String,
    @SerializedName("affiliation") @PropertyElement val affiliation: String?
)