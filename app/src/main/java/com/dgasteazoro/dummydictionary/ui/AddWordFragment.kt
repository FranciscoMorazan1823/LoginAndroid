package com.dgasteazoro.dummydictionary.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dgasteazoro.dummydictionary.DummyDictionaryApplication
import com.dgasteazoro.dummydictionary.R
import com.dgasteazoro.dummydictionary.data.model.Word
import com.dgasteazoro.dummydictionary.databinding.FragmentAddWordBinding

class AddWordFragment : Fragment() {
    private val viewModelFactory by lazy {
        val application = requireActivity().application as DummyDictionaryApplication
        WordViewModel(application.getDictionaryRepository())
    }
    //private val viewModel: WordViewModel by viewModels {
    //    viewModelFactory

   // }
   private val viewModel: WordViewModel by viewModels()

    private lateinit var binding: FragmentAddWordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_word, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        binding.addWordButton.setOnClickListener {
            viewModel.addWord(
                Word(
                    binding.wordText.text.toString(),
                    binding.definitionText.text.toString(),
                    false
                )
            )
            val action = AddWordFragmentDirections.actionAddWordFragmentToWordListFragment()
            navController.navigate(action)
        }


    }
}

