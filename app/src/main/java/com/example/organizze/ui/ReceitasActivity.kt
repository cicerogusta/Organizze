package com.example.organizze.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.example.organizze.base.BaseActivity
import com.example.organizze.data.model.Movimentacao
import com.example.organizze.databinding.ActivityReceitasBinding
import com.example.organizze.util.UiState
import com.example.organizze.util.dataAtual
import com.example.organizze.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceitasActivity : BaseActivity<ReceitasActivityViewModel, ActivityReceitasBinding>() {
    override val viewModel: ReceitasActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val movimentacao = Movimentacao(
            binding.editDataReceitas.text.toString(),
            binding.editCategoriaReceitas.text.toString(),
            binding.editDescricaoReceitas.text.toString(),
            "r",
            binding.editTotalReceitas.text.toString().toDouble()
        )
        viewModel.salvarReceita(movimentacao)
        resultadoSalvarReceita()
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

}