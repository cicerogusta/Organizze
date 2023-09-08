package com.example.organizze.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.example.organizze.base.BaseActivity
import com.example.organizze.data.model.Movimentacao
import com.example.organizze.databinding.ActivityDespesasBinding
import com.example.organizze.util.UiState
import com.example.organizze.util.dataAtual
import com.example.organizze.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DespesasActivity : BaseActivity<DespesasActivityViewModel, ActivityDespesasBinding>() {
    override val viewModel: DespesasActivityViewModel by viewModels()
    private var despesaTotal: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.editDataDespesas.setText(dataAtual())
        recuperarDespesaTotal()
        setupClickListener()
    }

    override fun getViewBinding(): ActivityDespesasBinding =
        ActivityDespesasBinding.inflate(layoutInflater)

    override fun setupClickListener() {
        binding.fabAdicionarDespesa.setOnClickListener {
           if (verificaCamposDespesas()) {
               salvarDespesa()
               finish()
           }
        }
    }

    private fun verificaCamposDespesas(): Boolean {
        return if (binding.editDataDespesas.text.toString().isNotEmpty()) {
            if (binding.editCategoriaDespesas.text.toString().isNotEmpty()) {
                if (binding.editDescricaoDespesas.text.toString().isNotEmpty()) {
                    if (binding.editTotalDespasas.text.toString().isNotEmpty()) {
                    } else {
                        toast("Preencha o total gasto!")
                        return false
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

    private fun salvarDespesa() {
        val valorRecuperado = binding.editTotalDespasas.text.toString().toDouble()
        val movimentacao = Movimentacao(
            binding.editDataDespesas.text.toString(),
            binding.editCategoriaDespesas.text.toString(),
            binding.editDescricaoDespesas.text.toString(),
            "d",
            valorRecuperado
        )
        val despesaAtualizada = despesaTotal + valorRecuperado
        atualizarDespesa(despesaAtualizada)
        viewModel.salvarDespesa(movimentacao)
        resultadoSalvarDespesa()
    }

    private fun atualizarDespesa(despesaAtualizada: Double) {
        viewModel.atualizarDespesa(despesaAtualizada)
    }

    private fun resultadoSalvarDespesa() {
        viewModel.movimentacaoDespesas.observe(this) { state ->
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

    private fun recuperarDespesaTotal() {
        viewModel.user.observe(this) {
            despesaTotal = it.despesaTotal
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.retornaUsuario()
    }

}