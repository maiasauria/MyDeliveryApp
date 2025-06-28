package com.mleon.core.data.datasource.local

import androidx.room.TypeConverter
import com.mleon.core.model.enums.Categories

class CategoriesListConverter {
    @TypeConverter
    fun fromList(list: List<Categories>?): String =
        list?.joinToString(",") { it.name } ?: ""

    @TypeConverter
    fun toList(data: String?): List<Categories> =
        data?.split(",")?.mapNotNull { runCatching { Categories.valueOf(it) }.getOrNull() } ?: emptyList()
}