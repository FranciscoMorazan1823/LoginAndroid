package com.dgasteazoro.dummydictionary.repository

import androidx.lifecycle.MutableLiveData
import com.dgasteazoro.dummydictionary.data.dao.AntonymDao
import com.dgasteazoro.dummydictionary.data.dao.SynonymDao
import com.dgasteazoro.dummydictionary.data.dao.WordDao
import com.dgasteazoro.dummydictionary.data.model.Word

class DictionaryRepository(
    private val wordDao: WordDao,
    val synonymDao: SynonymDao,
    val antonymDao: AntonymDao
) {

    fun getAllWords() = wordDao.getAllWords()

    suspend fun addWord(word: Word) {
        wordDao.insertWord(word)
    }
}