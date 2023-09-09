package com.example.organizze.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.organizze.data.model.Movimentacao
import com.example.organizze.util.UiState
import com.example.organizze.data.model.User
import com.google.firebase.database.ValueEventListener

interface FirebaseRepository {
    fun loginUser(email: String, senha: String, result: (UiState<String>) -> Unit)
    fun registerUser(user: User, result: (UiState<String>) -> Unit)
    fun getUserId(): String?
    fun isCurrentUser(): Boolean
    fun saveMovement(movimentacao: Movimentacao, result: (UiState<String>) -> Unit)

    fun getUser(mtbUser: MutableLiveData<User>)
    fun updateExpense(despesaAtualizada: Double)
    fun updateRecipe(receitaAtualizada: Double)
    fun logout()
    fun removeValueEventListenerUsuario(mtbEventListenerUsuario: MutableLiveData<ValueEventListener>)
    fun getEventListenerUsuario(eventListener: ValueEventListener)

}