package com.cicerodev.yourmoney.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.cicerodev.yourmoney.base.BaseActivity
import com.cicerodev.yourmoney.data.model.CartaoCredito
import com.cicerodev.yourmoney.databinding.ActivityCadastraCartaoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CadastraCartaoActivity : BaseActivity<CadastraCartaoActivityViewModel, ActivityCadastraCartaoBinding>() {
    override val viewModel: CadastraCartaoActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListener()
    }

    private fun atualizarCartoesUsuario(cartaoCredito: CartaoCredito) {
        viewModel.user.observe(this) {
            it.cartoesCredito.add(cartaoCredito)
        }
    }

    override fun getViewBinding(): ActivityCadastraCartaoBinding = ActivityCadastraCartaoBinding.inflate(layoutInflater)



    override fun setupClickListener() {
        binding.buttonAdicionarCartaoCadastro.setOnClickListener {

            val cartaoCredito = CartaoCredito(binding.editNomeCartaoCadastro.text.toString(), binding.editDataVencCartaoCadastro.text.toString(), binding.editLimiteCartaoCadastro.text.toString())

            viewModel.cadastrarCartao(cartaoCredito)
            atualizarCartoesUsuario(cartaoCredito)
            finish()
        }
    }
}