package com.cicerodev.yourmoney.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.yourmoney.data.model.CartaoCredito
import com.cicerodev.yourmoney.data.model.User
import com.cicerodev.yourmoney.data.repository.FirebaseRepository
import com.cicerodev.yourmoney.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CadastraCartaoActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    private val _UIState = MutableLiveData<UiState<String>>()
    val uiState: LiveData<UiState<String>>
        get() = _UIState

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun returnUser() {
        repository.getUser(_user)
    }

    fun cadastrarCartao(cartaoCredito: CartaoCredito) {

        repository.createCard(cartaoCredito) {
            _UIState.value = it
        }
    }

}