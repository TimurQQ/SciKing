package com.ilyasov.sci_king.data.db.cache

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ilyasov.sci_king.SciKingApplication
import com.ilyasov.sci_king.domain.entity.Author
import com.ilyasov.sci_king.domain.entity.Category
import com.ilyasov.sci_king.domain.entity.Link
import javax.inject.Inject

class Converters {
    @Inject
    lateinit var gson: Gson

    init {
        SciKingApplication.appComponent.inject(this)
    }

    @TypeConverter
    fun categoryToString(category: Category) = category.name

    @TypeConverter
    fun categoryFromString(value: String) = Category(value)

    @TypeConverter
    fun authorsFromString(value: String): MutableList<Author> =
        gson.fromJson(value, Array<Author>::class.java).toMutableList()

    @TypeConverter
    fun authorsToString(list: List<Author>): String = gson.toJson(list)

    @TypeConverter
    fun linksFromString(value: String): MutableList<Link> =
        gson.fromJson(value, Array<Link>::class.java).toMutableList()

    @TypeConverter
    fun linksToString(list: List<Link>): String = gson.toJson(list)
}