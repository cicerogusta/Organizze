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
class DespesasActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {
    private val _movimentacaoDespesas = MutableLiveData<UiState<String>>()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user
    val movimentacaoDespesas: LiveData<UiState<String>>
        get() = _movimentacaoDespesas
    fun salvarDespesa(movimentacao: Movimentacao) {
        _movimentacaoDespesas.value = UiState.Loading
        repository.saveMovement(
            movimentacao
        ) {
            _movimentacaoDespesas.value = it
        }
    }

    fun retornaUsuario() {
         repository.getUser(_user)
    }

    fun atualizarDespesa(despesaAtualizada: Double) {
        repository.updateExpense(despesaAtualizada)
    }
}