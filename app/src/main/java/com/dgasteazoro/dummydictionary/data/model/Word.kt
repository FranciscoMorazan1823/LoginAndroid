package com.dgasteazoro.dummydictionary.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
data class Word(
    @PrimaryKey @ColumnInfo(name = "term") val term: String,
    @ColumnInfo(name = "definition") val definition: String,
    @ColumnInfo(name = "is_fav", defaultValue = "0")  var isFav: Boolean
)