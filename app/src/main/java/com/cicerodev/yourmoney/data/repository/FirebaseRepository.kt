package com.cicerodev.yourmoney.data.repository

import androidx.lifecycle.MutableLiveData
import com.cicerodev.yourmoney.data.model.CartaoCredito
import com.cicerodev.yourmoney.data.model.Movimentacao
import com.cicerodev.yourmoney.data.model.User
import com.cicerodev.yourmoney.util.UiState
import com.google.firebase.database.ValueEventListener

interface FirebaseRepository {

    fun loginUser(email: String, senha: String, result: (UiState<String>) -> Unit)
    fun registerUser(user: User, result: (UiState<String>) -> Unit)
    fun getUserId(): String?
    fun isCurrentUser(): Boolean
    fun saveMovement(movimentacao: Movimentacao, result: (UiState<String>) -> Unit)

    fun createCard(cartaoCredito: CartaoCredito)
    fun getUser(mtbUser: MutableLiveData<User>)
    fun updateExpense(despesaAtualizada: Double)
    fun updateRecipe(receitaAtualizada: Double)
    fun removeCardLimit(limiteAtualizado: Double, cartaoCredito: CartaoCredito)
    fun removeCard(cartaoCredito: CartaoCredito)
    fun logout()
    fun getCards(): MutableLiveData<MutableList<CartaoCredito>>
    fun updateCard(cartaoCredito: CartaoCredito, novoLimite: Double)
    fun removeValueEventListenerUsuario(mtbEventListenerUsuario: MutableLiveData<ValueEventListener>)
    fun removeValueEventListenerMovements(mtbEventListenerMovements: MutableLiveData<ValueEventListener>, mesAnoSelecionado: String)
    fun getEventListenerUsuario(eventListener: ValueEventListener)
    fun getEventListenerMovements(eventListener: ValueEventListener)
    fun getMovements(mesAnoSelecionado: String): MutableLiveData<MutableList<Movimentacao>>
    fun removeMovement(mesAnoSelecionado: String, key: String)
    fun updateTotalRecipe(totalRecipe: Double)
    fun updateTotalExpense(totalExpense: Double)

    fun updateCardLimit(limiteAtualizado: Double, cartaoCredito: CartaoCredito)
}