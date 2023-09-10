package com.cicerodev.yourmoney.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.yourmoney.data.repository.FirebaseRepository
import com.cicerodev.yourmoney.util.UiState
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