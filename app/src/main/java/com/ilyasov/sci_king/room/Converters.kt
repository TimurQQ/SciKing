package com.ilyasov.sci_king.room

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.ilyasov.sci_king.model.Author
import com.ilyasov.sci_king.model.Category
import com.ilyasov.sci_king.model.Link

class Converters {
    private var builder = GsonBuilder().setPrettyPrinting()
    private var gson = builder.create()

    @TypeConverter
    fun categoryToString(category: Category) = category.name

    @TypeConverter
    fun categoryFromString(value: String) = Category(value)

    @TypeConverter
    fun authorsFromString(value: String): List<Author> =
        gson.fromJson(value, Array<Author>::class.java).toList()

    @TypeConverter
    fun authorsToString(list: List<Author>): String = gson.toJson(list)

    @TypeConverter
    fun linksFromString(value: String): List<Link> =
        gson.fromJson(value, Array<Link>::class.java).toList()

    @TypeConverter
    fun linksFromString(list: List<Link>): String = gson.toJson(list)
}