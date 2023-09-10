package com.cicerodev.yourmoney.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.yourmoney.data.model.User
import com.cicerodev.yourmoney.data.repository.FirebaseRepository
import com.cicerodev.yourmoney.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CadastroActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    fun cadastrarUsuario(user: User) {
//        _register.value = UiState.Loading
        repository.registerUser(user) {

            _register.value = it

        }

    }
}