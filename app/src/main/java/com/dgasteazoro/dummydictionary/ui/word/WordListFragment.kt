package com.dgasteazoro.dummydictionary.ui.word

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.dgasteazoro.dummydictionary.DummyDictionaryApplication
import com.dgasteazoro.dummydictionary.R
import com.dgasteazoro.dummydictionary.databinding.FragmentWordListBinding
import com.dgasteazoro.dummydictionary.ui.ViewModelFactory
import com.dgasteazoro.dummydictionary.ui.WordViewModel

class WordListFragment : Fragment() {
    private val viewModelFactory by lazy {
        val application = requireActivity().application as DummyDictionaryApplication
        ViewModelFactory(application.getDictionaryRepository())
    }
    private val viewModel: WordViewModel by viewModels {
viewModelFactory
    }
    private lateinit var binding: FragmentWordListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_word_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wordListRecyclerView = binding.wordListRecyclerView
        val wordAdapter = WordAdapter()
        wordListRecyclerView.apply {
            adapter = wordAdapter
        }

        viewModel.getAllWords()

        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                is WordUiState.Error -> Log.d("Word List Status", "Error", status.exception)
                WordUiState.Loading -> Log.d("Word List Status", "Loading")
                is WordUiState.Success -> handleSuccess(status, wordAdapter)

            }
        }
        }
        private fun handleSuccess(status: WordUiState.Success, wordAdapter: WordAdapter){
            status.word.observe(viewLifecycleOwner){
                data->
                wordAdapter.setData(data)
            }
        }

}