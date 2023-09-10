package com.cicerodev.yourmoney.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cicerodev.yourmoney.data.model.Movimentacao
import com.cicerodev.yourmoney.data.model.User
import com.cicerodev.yourmoney.data.repository.FirebaseRepository
import com.cicerodev.yourmoney.util.UiState
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