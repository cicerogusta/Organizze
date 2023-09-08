package com.example.organizze.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.organizze.data.model.Movimentacao
import com.example.organizze.data.model.User
import com.example.organizze.data.repository.FirebaseRepository
import com.example.organizze.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReceitasActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _movimentacaoReceitas = MutableLiveData<UiState<String>>()
    val movimentacaoReceitas: LiveData<UiState<String>>
        get() = _movimentacaoReceitas
     fun salvarReceita(movimentacao: Movimentacao) {
         _movimentacaoReceitas.value = UiState.Loading
         repository.saveMovement(
             movimentacao
         ) {
            _movimentacaoReceitas.value = it
         }
    }

    fun retornaUsuario() {
        repository.getUser(_user)
    }

    fun atualizarReceita(receitaAtualizada: Double) {
        repository.updateRecipe(receitaAtualizada)
    }
}