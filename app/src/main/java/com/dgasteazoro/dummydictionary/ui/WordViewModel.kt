package com.dgasteazoro.dummydictionary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgasteazoro.dummydictionary.model.Word
import com.dgasteazoro.dummydictionary.repository.DictionaryRepository

class WordViewModel(private val repository: DictionaryRepository): ViewModel() {
    val words = repository.words
    fun addWord(){
       val word =  Word("efimero", "Que dura poco tiempo o es pasajero.")
        repository.addWord(word)
    }
}

class WordViewModelFactory(private val repository: DictionaryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}


