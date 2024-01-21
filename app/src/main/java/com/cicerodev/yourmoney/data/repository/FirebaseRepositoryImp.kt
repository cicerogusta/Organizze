package com.cicerodev.yourmoney.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cicerodev.yourmoney.data.model.CartaoCredito
import com.cicerodev.yourmoney.data.model.Movimentacao
import com.cicerodev.yourmoney.data.model.User
import com.cicerodev.yourmoney.util.UiState
import com.cicerodev.yourmoney.util.codificarBase64
import com.cicerodev.yourmoney.util.formataData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener

class FirebaseRepositoryImp(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
) : FirebaseRepository {
    override fun registerUser(user: User, result: (UiState<String>) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.senha).addOnCompleteListener {
            if (it.isSuccessful) {
                atualizarNomeUsuario(user.nome)
                database.reference.child("usuarios").child(getUserId()!!).setValue(user)
                result.invoke(UiState.Success("Registrado com Sucesso"))
            } else {
                var exception = ""
                try {
                    throw it.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    exception = "Digite uma senha mais forte!"
                    result.invoke(UiState.Failure(exception))
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    exception = "Por favor, digite um e-mail valido!"
                    result.invoke(UiState.Failure(exception))
                } catch (e: FirebaseAuthUserCollisionException) {
                    exception = "Esta conta já foi cadastrada!"
                    result.invoke(UiState.Failure(exception))
                } catch (e: Exception) {
                    exception = "Erro ao cadastrar usuário " + e.message
                    result.invoke(UiState.Failure(exception))
                }
            }

        }


    }



    override fun updateCard(cartaoCredito: CartaoCredito, novoLimite: Double) {
        database.reference.child("cartoesCredito").child(getUserId()!!).child(cartaoCredito.key).child("limiteCartao").setValue(novoLimite.toString())
    }

    override fun loginUser(
        email: String,
        senha: String,
        result: (UiState<String>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.invoke(UiState.Success("Logado com Sucesso"))
                } else {
                    var exception = ""
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidUserException) {
                        exception = "Usuário não está cadastrado"
                        result.invoke(UiState.Failure(exception))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        exception = "E-mail e senha não correspondem a um usuário cadastrado!"
                        result.invoke(UiState.Failure(exception))
                    } catch (e: Exception) {
                        exception = "Erro ao logar usuário " + e.message
                        result.invoke(UiState.Failure(exception))
                    }
                }
            }
    }

    override fun getUserId(): String? {
        return auth.currentUser?.email?.let { codificarBase64(it) }
    }

    private fun atualizarNomeUsuario(nome: String?): Boolean {
        return try {
            val user = auth.currentUser
            val profile = UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build()
            user!!.updateProfile(profile).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("Perfil", "Erro ao atualizar nome de perfil.")
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun isCurrentUser(): Boolean {
        var isCurrentUser = false
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            isCurrentUser = true

        }
        return isCurrentUser
    }

    override fun saveMovement(movimentacao: Movimentacao, result: (UiState<String>) -> Unit) {
        val idUsuario = getUserId()
        val mesAno = formataData(movimentacao.data)
        if (idUsuario != null) {
            val movimentacaoRef = database.reference.child("movimentacao")
                .child(idUsuario).child(mesAno).push().setValue(movimentacao)

            movimentacaoRef.addOnCompleteListener {
                if (it.isSuccessful) {
                    if (movimentacao.tipo == "r") {
                        result.invoke(UiState.Success("Receita adicionada com sucesso"))
                    } else {
                        if (movimentacao.tipo == "d") {
                            result.invoke(UiState.Success("Despesa adicionada com sucesso"))
                        }
                    }
                } else {
                    if (movimentacao.tipo == "r") {
                        result.invoke(UiState.Failure("Não foi possivel adicionar receita"))

                    } else {
                        if (movimentacao.tipo == "d") {
                            result.invoke(UiState.Failure("Não foi possivel adicionar despesa"))

                        }
                    }

                }
            }


        }
    }

    override fun createCard(cartaoCredito: CartaoCredito) {
        val reference = database.reference.child("cartoesCredito")
            .child(getUserId()!!)
        val push = reference.push()
        val key = push.key
        cartaoCredito.key = key!!
        push.setValue(cartaoCredito)
    }



    override fun getUser(mtbUser: MutableLiveData<User>) {
        val idUsuario = getUserId()!!
        val usuarioRef = database.reference.child("usuarios").child(idUsuario)
        val eventListener = usuarioRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(User::class.java)
                mtbUser.postValue(usuario)


            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        getEventListenerUsuario(eventListener)
    }

    override fun getCards(): MutableLiveData<MutableList<CartaoCredito>> {
        val liveData: MutableLiveData<MutableList<CartaoCredito>> = MutableLiveData()
        val eventListener =
            database.reference.child("cartoesCredito").child(getUserId()!!)
                .addValueEventListener(object : ValueEventListener {
                    val listaCartoes = mutableListOf<CartaoCredito>()
                    override fun onDataChange(snapshot: DataSnapshot) {
                        listaCartoes.clear()
                        liveData.value?.clear()
                        for (cartoes in snapshot.children) {
                            val cartao = cartoes.getValue(CartaoCredito::class.java)
                            cartao?.key = cartoes.key.toString()
                            if (cartao != null) {
                                listaCartoes.add(cartao)
                                Log.d("lista", listaCartoes.toString())

                            }
                        }
                        liveData.postValue(listaCartoes)


                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        getEventListenerMovements(eventListener)
        return liveData

    }




    override fun updateExpense(despesaAtualizada: Double) {
        val idUsuario = getUserId()
        val usuarioRef = idUsuario?.let { database.reference.child("usuarios").child(it) }

        usuarioRef?.child("despesaTotal")?.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                // Obter o valor atual do nó
                val currentValue = mutableData.getValue(Double::class.java)

                // Adicionar o valor desejado
                val newValue = (currentValue ?: 0.0) + despesaAtualizada

                // Definir o novo valor no nó
                mutableData.value = newValue

                // Retornar sucesso
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                if (databaseError != null) {
                    // Tratar erro na transação
                } else {
                    // Transação bem-sucedida
                }
            }
        })
    }

    override fun updateRecipe(receitaAtualizada: Double) {
        val idUsuario = getUserId()
        val usuarioRef = idUsuario?.let { database.reference.child("usuarios").child(it) }

        usuarioRef?.child("receitaTotal")?.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                // Obter o valor atual do nó
                val currentValue = mutableData.getValue(Double::class.java)

                // Adicionar o valor desejado
                val newValue = (currentValue ?: 0.0) + receitaAtualizada

                // Definir o novo valor no nó
                mutableData.value = newValue

                // Retornar sucesso
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                if (databaseError != null) {
                    // Tratar erro na transação
                } else {
                    // Transação bem-sucedida
                }
            }
        })
    }

    override fun updateCardLimit(limiteAtualizado: Double, cartaoCredito: CartaoCredito) {
        val idUsuario = getUserId()
        val usuarioRef = database.reference.child("cartoesCredito").child(idUsuario!!).child(cartaoCredito.key)

        usuarioRef.child("limiteCartao").runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                // Obter o valor atual do nó
                val currentValue = mutableData.getValue(String::class.java)

                // Adicionar o valor desejado
                val newValue = (currentValue?.toDouble() ?: 0.0) + limiteAtualizado

                // Definir o novo valor no nó
                mutableData.value = newValue.toString()

                // Retornar sucesso
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                if (databaseError != null) {
                    // Tratar erro na transação
                } else {
                    // Transação bem-sucedida
                }
            }
        })
    }

    override fun logout() {
        auth.signOut()
    }

    override fun removeValueEventListenerUsuario(mtbEventListenerUsuario: MutableLiveData<ValueEventListener>) {
        mtbEventListenerUsuario.value?.let {
            database.reference.child("usuarios").child(getUserId()!!).removeEventListener(
                it
            )
        }


    }

    override fun removeValueEventListenerMovements(
        mtbEventListenerMovements: MutableLiveData<ValueEventListener>,
        mesAnoSelecionado: String
    ) {
        mtbEventListenerMovements.value?.let {
            database.reference.child("movimentacao").child(getUserId()!!).child(mesAnoSelecionado)
                .removeEventListener(
                    it
                )
        }
    }

    override fun getEventListenerUsuario(eventListener: ValueEventListener) {
        val mtbEventListener = MutableLiveData<ValueEventListener>()
        mtbEventListener.postValue(eventListener)
    }

    override fun getEventListenerMovements(eventListener: ValueEventListener) {
        val mtbEventListener = MutableLiveData<ValueEventListener>()
        mtbEventListener.postValue(eventListener)
    }

    override fun getMovements(mesAnoSelecionado: String): MutableLiveData<MutableList<Movimentacao>> {
        val liveData: MutableLiveData<MutableList<Movimentacao>> = MutableLiveData()
        val eventListener =
            database.reference.child("movimentacao").child(getUserId()!!).child(mesAnoSelecionado)
                .addValueEventListener(object : ValueEventListener {
                    val listaMovimentacoes = mutableListOf<Movimentacao>()
                    override fun onDataChange(snapshot: DataSnapshot) {
                        listaMovimentacoes.clear()
                        liveData.value?.clear()
                        for (movimentacoes in snapshot.children) {
                            val movimentacao = movimentacoes.getValue(Movimentacao::class.java)
                            movimentacao?.key = movimentacoes.key.toString()
                            if (movimentacao != null) {
                                listaMovimentacoes.add(movimentacao)

                            }
                        }
                        liveData.postValue(listaMovimentacoes)


                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        getEventListenerMovements(eventListener)
        return liveData
    }

    override fun removeMovement(mesAnoSelecionado: String, key: String) {
        database.reference.child("movimentacao").child(getUserId()!!).child(mesAnoSelecionado)
            .child(key).removeValue()
    }

    override fun updateTotalRecipe(totalRecipe: Double) {
        database.reference.child("usuarios").child(getUserId()!!)
            .child("receitaTotal").setValue(totalRecipe)
    }

    override fun updateTotalExpense(totalExpense: Double) {
        database.reference.child("usuarios").child(getUserId()!!)
            .child("despesaTotal").setValue(totalExpense)
    }

}
