package com.example.organizze.ui

import androidx.activity.viewModels
import com.example.organizze.base.BaseActivity
import com.example.organizze.data.model.User
import com.example.organizze.databinding.ActivityLoginBinding
import com.example.organizze.util.UiState
import com.example.organizze.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginActivityViewModel, ActivityLoginBinding>() {
    override val viewModel: LoginActivityViewModel by viewModels()

    override fun getViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

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
                }
            }
        }
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