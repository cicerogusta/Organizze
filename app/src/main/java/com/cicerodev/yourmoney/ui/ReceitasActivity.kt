package com.cicerodev.yourmoney.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.cicerodev.yourmoney.base.BaseActivity
import com.cicerodev.yourmoney.data.model.Movimentacao
import com.cicerodev.yourmoney.databinding.ActivityReceitasBinding
import com.cicerodev.yourmoney.util.UiState
import com.cicerodev.yourmoney.util.dataAtual
import com.cicerodev.yourmoney.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceitasActivity : BaseActivity<ReceitasActivityViewModel, ActivityReceitasBinding>() {
    override val viewModel: ReceitasActivityViewModel by viewModels()
    private var receitaTotal: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recuperarReceitaTotal()
        binding.editDataReceitas.setText(dataAtual())
        setupClickListener()
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
        val valorRecuperado = binding.editTotalReceitas.text.toString().toDouble()
        val movimentacao = Movimentacao(
            binding.editDataReceitas.text.toString(),
            binding.editCategoriaReceitas.text.toString(),
            binding.editDescricaoReceitas.text.toString(),
            "r",
           valorRecuperado
        )
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

    private fun recuperarReceitaTotal() {
        viewModel.user.observe(this) {
            receitaTotal = it.despesaTotal
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.retornaUsuario()
    }

}