package com.example.organizze.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.organizze.data.model.Movimentacao
import com.example.organizze.util.UiState
import com.example.organizze.data.model.User
import com.example.organizze.util.codificarBase64
import com.example.organizze.util.mesAnoDataEscolhida
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
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
        val mesAno = mesAnoDataEscolhida(movimentacao.data)
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

    override fun getUser(mtbUser: MutableLiveData<User>) {
        val idUsuario = getUserId()
        idUsuario?.let { database.reference.child("usuarios").child(it) }
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val usuario = snapshot.getValue(User::class.java)
                    mtbUser.postValue(usuario)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    override fun updateExpense(despesaAtualizada: Double) {
        val idUsuario = getUserId()
        val usuarioRef = idUsuario?.let { database.reference.child("usuarios").child(it) }

        usuarioRef?.child("despesaTotal")?.setValue(despesaAtualizada)
    }

    override fun updateRecipe(receitaAtualizada: Double) {
        val idUsuario = getUserId()
        val usuarioRef = idUsuario?.let { database.reference.child("usuarios").child(it) }

        usuarioRef?.child("receitaTotal")?.setValue(receitaAtualizada)
    }

    override fun logout() {
        auth.signOut()
    }

}
