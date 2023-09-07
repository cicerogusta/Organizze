package com.example.organizze.data.repository

import android.util.Log
import com.example.organizze.util.UiState
import com.example.organizze.data.model.User
import com.example.organizze.util.codificarBase64
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

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
                }catch (e: FirebaseAuthWeakPasswordException) {
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

}
