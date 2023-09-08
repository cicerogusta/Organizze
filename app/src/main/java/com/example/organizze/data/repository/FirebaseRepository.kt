package com.example.organizze.data.repository

import com.example.organizze.data.model.Movimentacao
import com.example.organizze.util.UiState
import com.example.organizze.data.model.User

interface FirebaseRepository {
    fun loginUser(email: String, senha: String, result: (UiState<String>) -> Unit)
    fun registerUser(user: User, result: (UiState<String>) -> Unit)
    fun getUserId(): String?
    fun isCurrentUser(): Boolean
    fun saveMovement(movimentacao: Movimentacao, result: (UiState<String>) -> Unit)

}