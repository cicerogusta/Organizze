package com.cicerodev.yourmoney.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.cicerodev.yourmoney.base.BaseActivity
import com.cicerodev.yourmoney.data.model.CartaoCredito
import com.cicerodev.yourmoney.databinding.ActivityCadastraCartaoBinding
import com.cicerodev.yourmoney.util.UiState
import com.cicerodev.yourmoney.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CadastraCartaoActivity : BaseActivity<CadastraCartaoActivityViewModel, ActivityCadastraCartaoBinding>() {
    private var cartaoCreditoExists: CartaoCredito? = null
    override val viewModel: CadastraCartaoActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupClickListener()
        viewModel.returnUser()
    }

    private fun resultadoCadastroCartao() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                }

                is UiState.Failure -> {
                    toast(state.error)
                }

                is UiState.Success -> {
                    toast(state.data)
                    finish()
                }
            }
        }
    }

    private fun verificaCamposCartao(): Boolean {
        return if (binding.editNomeCartaoCadastro.text.toString().isNotEmpty()) {
            if (binding.editDataVencCartaoCadastro.text.toString().isNotEmpty()) {
                if (binding.editLimiteCartaoCadastro.text.toString().isNotEmpty()) {
                    return true
                } else {
                    toast("Preencha o limite!")
                    return false
                }
            } else {
                toast("Preencha a data!")
                false
            }
        } else {
            toast("Preencha o nome do Cartão!")
            return false
        }
    }

    override fun getViewBinding(): ActivityCadastraCartaoBinding = ActivityCadastraCartaoBinding.inflate(layoutInflater)



    override fun setupClickListener() {
        binding.buttonAdicionarCartaoCadastro.setOnClickListener {

            val cartaoCredito = CartaoCredito(binding.editNomeCartaoCadastro.text.toString(), binding.editDataVencCartaoCadastro.text.toString(), binding.editLimiteCartaoCadastro.text.toString())

            if (verificaCamposCartao()) {
                viewModel.cadastrarCartao(cartaoCredito)
                resultadoCadastroCartao()


            }

        }
    }
}