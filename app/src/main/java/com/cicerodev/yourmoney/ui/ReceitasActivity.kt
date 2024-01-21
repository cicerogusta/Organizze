package com.cicerodev.yourmoney.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.cicerodev.yourmoney.base.BaseActivity
import com.cicerodev.yourmoney.data.model.CartaoCredito
import com.cicerodev.yourmoney.data.model.Movimentacao
import com.cicerodev.yourmoney.databinding.ActivityReceitasBinding
import com.cicerodev.yourmoney.util.MoneyTextWatcher
import com.cicerodev.yourmoney.util.UiState
import com.cicerodev.yourmoney.util.dataAtual
import com.cicerodev.yourmoney.util.removePoints
import com.cicerodev.yourmoney.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceitasActivity : BaseActivity<ReceitasActivityViewModel, ActivityReceitasBinding>() {
    override val viewModel: ReceitasActivityViewModel by viewModels()
    private var receitaTotal: Double = 0.0
    private var isReceitaCartao = false
    private var isReceitaDinheiro = false
    private var isReceitaPix = false
    private var cartaoCredito: CartaoCredito? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.editTotalReceitas.addTextChangedListener(MoneyTextWatcher(binding.editTotalReceitas))
        binding.editDataReceitas.setText(dataAtual())
        setupClickListener()

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                binding.radioButtonPix.id -> {
                    isReceitaPix = true
                }

                binding.radioButtonDinheiro.id -> {
                    isReceitaDinheiro = true
                }

                binding.radioButtonCartao.id -> {
                    isReceitaCartao = true

                }
            }
        }
    }

    override fun getViewBinding(): ActivityReceitasBinding = ActivityReceitasBinding.inflate(layoutInflater)

    override fun setupClickListener() {
        binding.fabAdicionarReceita.setOnClickListener {
            if (verificaCamposReceitas()) {
                salvarReceita()
                finish()
            }
        }
    }

    private fun verificaCamposReceitas(): Boolean {
        return if (binding.editDataReceitas.text.toString().isNotEmpty()) {
            if (binding.editCategoriaReceitas.text.toString().isNotEmpty()) {
                if (binding.editDescricaoReceitas.text.toString().isNotEmpty()) {
                    if (binding.editTotalReceitas.text.toString().isNotEmpty()) {
                    } else {
                        toast("Preencha o total gasto!")
                        return  false
                    }
                } else {
                    toast("Preencha a descrição!")
                     return false
                }
                true
            } else {
                toast("Preencha a categoria!")
                false
            }
        } else {
            toast("Preencha a data!")
            return false
        }
    }

    private fun salvarReceita() {
        val valorRecuperado = removePoints(binding.editTotalReceitas.text.toString()).toDouble()
        val movimentacao = Movimentacao(
            binding.editDataReceitas.text.toString(),
            binding.editCategoriaReceitas.text.toString(),
            binding.editDescricaoReceitas.text.toString(),
            "r",
           valorRecuperado
        )
        if (isReceitaCartao) {
            movimentacao.isReceitaCartao = true
        } else {
            if (isReceitaDinheiro) {
                movimentacao.isDespesaDinheiro = true
            } else {
                if (isReceitaPix) {
                    movimentacao.isDespesaPix = true
                }
            }
        }
        val receitaAtualizada = receitaTotal + valorRecuperado
        atualizarReceita(receitaAtualizada)
        viewModel.salvarReceita(movimentacao)
        resultadoSalvarReceita()
    }

    private fun atualizarReceita(despesaAtualizada: Double) {
        viewModel.atualizarReceita(despesaAtualizada)
    }

    private fun resultadoSalvarReceita() {
        viewModel.movimentacaoReceitas.observe(this) { state ->
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

    override fun onStart() {
        super.onStart()
        viewModel.retornaUsuario()
    }

}