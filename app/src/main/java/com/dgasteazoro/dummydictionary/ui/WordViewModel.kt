package com.dgasteazoro.dummydictionary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dgasteazoro.dummydictionary.data.model.Word
import com.dgasteazoro.dummydictionary.repository.DictionaryRepository
import kotlinx.coroutines.launch

class WordViewModel(private val repository: DictionaryRepository): ViewModel() {
    val words = repository.getAllWords()

    fun addWord(word: Word) {
        viewModelScope.launch {
            repository.addWord(word)
        }
    }
}

class WordViewModelFactory(private val repository: DictionaryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}


