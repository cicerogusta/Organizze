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
class CadastroActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    fun cadastrarUsuario(user: User) {
        _register.value = UiState.Loading
        repository.registerUser(user) {

            _register.value = it

        }

    }
}