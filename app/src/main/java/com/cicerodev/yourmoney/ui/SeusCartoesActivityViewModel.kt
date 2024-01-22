package com.cicerodev.yourmoney.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.yourmoney.data.model.CartaoCredito
import com.cicerodev.yourmoney.data.model.User
import com.cicerodev.yourmoney.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class SeusCartoesActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _cartoesCredito = MutableLiveData<MutableList<CartaoCredito>>()
    val cartoesCredito: LiveData<MutableList<CartaoCredito>>
        get() = _cartoesCredito

    fun returnUser() {
        repository.getUser(_user)
    }

    fun returnCards(): MutableLiveData<MutableList<CartaoCredito>> {
       return repository.getCards()
    }

    fun removerCartao(cartaoCredito: CartaoCredito) {
        repository.removeCard(cartaoCredito)
    }

}