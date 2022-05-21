package com.dgasteazoro.dummydictionary.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dgasteazoro.dummydictionary.data.DummyDictionaryDatabase
import com.dgasteazoro.dummydictionary.data.dao.AntonymDao
import com.dgasteazoro.dummydictionary.data.dao.SynonymDao
import com.dgasteazoro.dummydictionary.data.dao.WordDao
import com.dgasteazoro.dummydictionary.data.model.Word
import com.dgasteazoro.dummydictionary.network.ApiResponse
import com.dgasteazoro.dummydictionary.network.WordService
import okio.IOException
import retrofit2.HttpException

class DictionaryRepository(
    database: DummyDictionaryDatabase,
    private val api: WordService
) {
    private val wordDao = database.wordDao()
    suspend fun getAllWords() : ApiResponse<LiveData<List<Word>>> {

        return try {
            val response = api.getAllWord()

            if (response.count > 0) {
                wordDao.insertWord(response.words)
            }
            ApiResponse.Success(data = wordDao.getAllWords())
        } catch (e: HttpException) {
            ApiResponse.Error(exception = e)
        } catch (e: IOException) {
            ApiResponse.Error(exception = e)
        }
    }

    suspend fun addWord(word: Word) {
        wordDao.insertWord(word)
    }
}