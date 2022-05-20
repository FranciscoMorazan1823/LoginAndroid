package com.dgasteazoro.dummydictionary.network.dto

import com.dgasteazoro.dummydictionary.data.model.Word
import com.google.gson.annotations.SerializedName

data class WordsResponse (
    @SerializedName("count")
    val count: Int,
    @SerializedName("words")
    val words: List<Word>
    )
