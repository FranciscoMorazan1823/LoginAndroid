package com.dgasteazoro.dummydictionary.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dgasteazoro.dummydictionary.DummyDictionaryApplication
import com.dgasteazoro.dummydictionary.MainActivity
import com.dgasteazoro.dummydictionary.R
import com.dgasteazoro.dummydictionary.databinding.ActivityLoginBinding
import com.dgasteazoro.dummydictionary.ui.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    val app by lazy {
        application as DummyDictionaryApplication
    }
    private val viewModelFactory by lazy {
        ViewModelFactory(app.getLoginRepository())
    }
    private val viewModel: LoginViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (app.isUserLogin()) {
            return startMainActivity()
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel

        viewModel.status.observe(this) { status ->
            handleUiState(status)
        }
    }

    private fun handleUiState(status: LoginUiStatus) {
        when (status) {
            is LoginUiStatus.Error -> Log.d("Login List Status", "Error")
            LoginUiStatus.Loading -> Log.d("Login List Status", "Loading")
            LoginUiStatus.Resume -> Log.d("Login List Status", "Resume")
            is LoginUiStatus.Success -> {
                app.saveAuthToken(status.token)
                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}