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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.editDataDespesas.setText(dataAtual())
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
        val movimentacao = Movimentacao(
            binding.editDataDespesas.text.toString(),
            binding.editCategoriaDespesas.text.toString(),
            binding.editDescricaoDespesas.text.toString(),
            "d",
            binding.editTotalDespasas.text.toString().toDouble()
        )
        viewModel.salvarDespesa(movimentacao)
        resultadoSalvarDespesa()
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

}