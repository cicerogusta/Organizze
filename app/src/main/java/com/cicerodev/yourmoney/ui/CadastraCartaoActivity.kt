package com.cicerodev.yourmoney.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.cicerodev.yourmoney.base.BaseActivity
import com.cicerodev.yourmoney.data.model.CartaoCredito
import com.cicerodev.yourmoney.databinding.ActivityCadastraCartaoBinding
import com.cicerodev.yourmoney.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CadastraCartaoActivity : BaseActivity<CadastraCartaoActivityViewModel, ActivityCadastraCartaoBinding>() {
    override val viewModel: CadastraCartaoActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListener()
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

    private fun atualizarCartoesUsuario(cartaoCredito: CartaoCredito) {
        viewModel.user.observe(this) {
            it.cartoesCredito.add(cartaoCredito)
        }
    }

    override fun getViewBinding(): ActivityCadastraCartaoBinding = ActivityCadastraCartaoBinding.inflate(layoutInflater)



    override fun setupClickListener() {
        binding.buttonAdicionarCartaoCadastro.setOnClickListener {

            if (verificaCamposCartao()) {
                val cartaoCredito = CartaoCredito(binding.editNomeCartaoCadastro.text.toString(), binding.editDataVencCartaoCadastro.text.toString(), binding.editLimiteCartaoCadastro.text.toString())
                // Obtém os primeiros dois caracteres (índices 0 e 1)
                val primeiraParte = cartaoCredito.dataVencimento?.substring(0, 2)
                if (primeiraParte != null) {
                    if (primeiraParte.toInt() > 12 || primeiraParte.toInt() == 0 ) {
                        toast("Mês inválido")
                    } else {
                        viewModel.cadastrarCartao(cartaoCredito)
                        atualizarCartoesUsuario(cartaoCredito)
                        finish()
                    }
            }


            }

        }
    }
}