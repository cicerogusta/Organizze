package com.example.organizze.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.organizze.data.model.User
import com.example.organizze.data.repository.FirebaseRepository
import com.example.organizze.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {
    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    fun login(
        email: String,
        senha: String
    ) {
        _login.value = UiState.Loading
        repository.loginUser(
            email, senha
        ) {
            _login.value = it
        }
    }
}