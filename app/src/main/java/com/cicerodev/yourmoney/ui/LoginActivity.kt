package com.cicerodev.yourmoney.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.cicerodev.yourmoney.base.BaseActivity
import com.cicerodev.yourmoney.data.model.User
import com.cicerodev.yourmoney.databinding.ActivityLoginBinding
import com.cicerodev.yourmoney.util.UiState
import com.cicerodev.yourmoney.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginActivityViewModel, ActivityLoginBinding>() {
    override val viewModel: LoginActivityViewModel by viewModels()

    override fun getViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListener()
    }
    override fun setupClickListener() {
        binding.buttonLogin.setOnClickListener {
            if (verificaCamposLogin()) {
                loginUsuario()
            }
        }
    }

    private fun loginUsuario() {
        val user = User(
            email = binding.editEmailLogin.text.toString(),
            senha = binding.editSenhaLogin.text.toString()
        )
        viewModel.login(user.email, user.senha)
        resultadoLogin()
    }

    private fun resultadoLogin() {
        viewModel.login.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                }

                is UiState.Failure -> {
                    toast(state.error)
                }

                is UiState.Success -> {
                    toast(state.data)
                    abrirTelaPrincipal()
                }
            }
        }
    }

    private fun abrirTelaPrincipal() {
        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }

    private fun verificaCamposLogin(): Boolean {
        return if (binding.editEmailLogin.text.toString().isNotEmpty()) {
            if (binding.editSenhaLogin.text.toString().isNotEmpty()) {
                true
            } else {
                toast("Preencha o email!")
                false
            }
        } else {
            toast("Preencha a senha!")
            return false
        }
    }

}