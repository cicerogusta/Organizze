package com.cicerodev.yourmoney.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.cicerodev.yourmoney.base.BaseActivity
import com.cicerodev.yourmoney.data.model.CartaoCredito
import com.cicerodev.yourmoney.data.model.User
import com.cicerodev.yourmoney.databinding.ActivityCadastroBinding
import com.cicerodev.yourmoney.util.UiState
import com.cicerodev.yourmoney.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CadastroActivity : BaseActivity<CadastroActivityViewModel, ActivityCadastroBinding>() {
    override val viewModel: CadastroActivityViewModel by viewModels()
    override fun getViewBinding(): ActivityCadastroBinding =
        ActivityCadastroBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListener()
    }

    override fun setupClickListener() {
        binding.buttonCadastrar.setOnClickListener {
            cadastrarUsuario()
        }
    }

    private fun verificaCamposRegistro(): Boolean {
        return if (binding.editNomeRegistro.text.toString().isNotEmpty()) {
             if (binding.editEmailRegistro.text.toString().isNotEmpty()) {
                if (binding.editSenhaRegistro.text.toString().isNotEmpty()) {
                    true
                } else {
                    toast("Preencha a senha")
                    false
                }
            } else {
                toast("Preencha o email!")
                false
            }
        } else {
            toast("Preencha o nome!")
            return false
        }
    }

    private fun cadastrarUsuario() {
        if (verificaCamposRegistro()) {
            val usuario = User(
                nome = binding.editNomeRegistro.text.toString(),
                email = binding.editEmailRegistro.text.toString(),
                senha = binding.editSenhaRegistro.text.toString(),
                cartoesCredito = mutableListOf(CartaoCredito("ttt", "18/01", "5000"), CartaoCredito("asassa","12/26", "8000"))
            )
            viewModel.cadastrarUsuario(usuario)
            resultadoRegistro()
        }
    }

    private fun resultadoRegistro() {
        viewModel.register.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
//                    binding.loginProgress.show()
                }

                is UiState.Failure -> {
//                    binding.loginProgress.hide()
                    toast(state.error)
                }

                is UiState.Success -> {
                    abrirTelaPrincipal()
                    toast(state.data)
                }
            }
        }
    }

    private fun abrirTelaPrincipal() {
        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }

}