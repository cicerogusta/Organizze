package com.example.organizze.data.repository

import android.util.Log
import com.example.organizze.util.UiState
import com.example.organizze.data.model.User
import com.example.organizze.util.codificarBase64
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepositoryImp(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
) : FirebaseRepository {
    override fun loginUser(email: String, senha: String, result: (UiState<String>) -> Unit) {

    }

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

}
