package com.dgasteazoro.dummydictionary

import android.app.Application
import com.dgasteazoro.dummydictionary.data.DummyDictionaryDatabase
import com.dgasteazoro.dummydictionary.repository.DictionaryRepository

class DummyDictionaryApplication : Application() {
    val dataBase by lazy {
        DummyDictionaryDatabase.getInstance(this)
    }

    fun getDictionaryRepository() = with(dataBase) {
        DictionaryRepository(wordDao(), synonymDao(), antonymDao())
    }
}