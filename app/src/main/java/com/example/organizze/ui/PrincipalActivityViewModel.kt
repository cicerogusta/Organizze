package com.example.organizze.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.organizze.data.model.User
import com.example.organizze.data.repository.FirebaseRepository
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrincipalActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _eventListenerUsuario = MutableLiveData<ValueEventListener>()
    fun sair() {
        repository.logout()
    }
    fun recuperarUsuario() {
        repository.getUser(_user)
    }
    fun removerEventListener() {
        repository.removeValueEventListenerUsuario(_eventListenerUsuario)
    }

    fun retornaEventListenerUsuario() {
        _eventListenerUsuario.value?.let { repository.getEventListenerUsuario(it) }
    }
}