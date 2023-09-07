package com.example.organizze.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.example.organizze.base.BaseActivity
import com.example.organizze.data.model.Movimentacao
import com.example.organizze.databinding.ActivityDespesasBinding
import com.example.organizze.util.dataAtual
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
            salvarDespesa()
            finish()
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
    }

}