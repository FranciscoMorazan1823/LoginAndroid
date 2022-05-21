package com.dgasteazoro.dummydictionary.ui

import androidx.lifecycle.*
import com.dgasteazoro.dummydictionary.data.model.Word
import com.dgasteazoro.dummydictionary.network.ApiResponse
import com.dgasteazoro.dummydictionary.repository.DictionaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(private val repository: DictionaryRepository): ViewModel() {

    private val _status = MutableLiveData<WordUiState>(WordUiState.Loading)
    val status: LiveData<WordUiState>
        get() = _status

    fun getAllWords() {
        viewModelScope.launch(Dispatchers.IO) {
            _status.postValue(
                when (val response = repository.getAllWords()) {
                    is ApiResponse.Error -> WordUiState.Error(response.exception)
                    is ApiResponse.Success -> WordUiState.Success(response.data)
                    is ApiResponse.ErrorWithMessage -> TODO()
                }
            )
        }
    }

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


