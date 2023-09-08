package com.example.organizze.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.organizze.data.model.Movimentacao
import com.example.organizze.data.repository.FirebaseRepository
import com.example.organizze.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReceitasActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

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
}