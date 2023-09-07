package com.example.organizze.ui

import androidx.lifecycle.ViewModel
import com.example.organizze.data.model.Movimentacao
import com.example.organizze.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DespesasActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {
        fun salvarDespesa(movimentacao: Movimentacao) {
            repository.saveExpanse(movimentacao)
        }
}