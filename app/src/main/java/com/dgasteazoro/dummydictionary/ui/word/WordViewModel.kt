package com.dgasteazoro.dummydictionary.ui

import androidx.lifecycle.*
import com.dgasteazoro.dummydictionary.data.model.Word
import com.dgasteazoro.dummydictionary.network.ApiResponse
import com.dgasteazoro.dummydictionary.repository.DictionaryRepository
import com.dgasteazoro.dummydictionary.ui.word.WordUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(private val repository: DictionaryRepository): ViewModel() {

    private val _status = MutableLiveData<WordUiState>(WordUiState.Loading)
    val status: LiveData<WordUiState>
        get() = _status

    fun getAllWords() {
        _status.value = WordUiState.Loading
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




