package com.cicerodev.yourmoney.ui

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.cicerodev.yourmoney.base.BaseActivity
import com.cicerodev.yourmoney.data.model.CartaoCredito
import com.cicerodev.yourmoney.data.model.Movimentacao
import com.cicerodev.yourmoney.databinding.ActivityDespesasBinding
import com.cicerodev.yourmoney.util.MoneyTextWatcher
import com.cicerodev.yourmoney.util.UiState
import com.cicerodev.yourmoney.util.dataAtual
import com.cicerodev.yourmoney.util.extractNumbersFromString
import com.cicerodev.yourmoney.util.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DespesasActivity : BaseActivity<DespesasActivityViewModel, ActivityDespesasBinding>() {
    override val viewModel: DespesasActivityViewModel by viewModels()
    private var despesaTotal: Double = 0.0
    private var cartaoCredito: CartaoCredito? = null
    private var isDespesaCartao: Boolean = false
    private var isDespesaPix: Boolean = false
    private var isDespesaDinheiro: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.editTotalDespasas.addTextChangedListener(MoneyTextWatcher(binding.editTotalDespasas))
        binding.editDataDespesas.setText(dataAtual())
        setupClickListener()
        configurarCartoes()


        viewModel.returnCards().observe(this) {
            val cardPosition = binding.spinner1.selectedItemPosition
            cartaoCredito = it[cardPosition]


        }

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                binding.radioButtonPix.id -> {
                    isDespesaPix = true
                    binding.spinner1.visibility = View.GONE
                }

                binding.radioButtonDinheiro.id -> {
                    isDespesaDinheiro = true
                    binding.spinner1.visibility = View.GONE
                }

                binding.radioButtonCartao.id -> {
                    isDespesaCartao = true
                    binding.spinner1.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun configurarCartoes() {

        viewModel.returnCards().observe(this) { cartaoCredito ->
            if (cartaoCredito.isNotEmpty()) {
                val items = mutableListOf<String>()
                cartaoCredito.forEach {
                    it.nomeCartao?.let { it1 -> items.add(it1) }

                }
                val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, items)
                binding.spinner1.adapter = adapter
            }




        }



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
        val valorRecuperado = extractNumbersFromString(binding.editTotalDespasas.text.toString()).toDouble()
        val movimentacao = Movimentacao(
            binding.editDataDespesas.text.toString(),
            binding.editCategoriaDespesas.text.toString(),
            binding.editDescricaoDespesas.text.toString(),
            "d",
            valorRecuperado
        )
        if (isDespesaCartao) {
            movimentacao.isDespesaCartao = true
            movimentacao.cartaoCredito = cartaoCredito!!
            val limiteTotalCartaoAtualizado =
                cartaoCredito?.limiteCartao!!.toDouble() - valorRecuperado
            viewModel.atualizarCartao(cartaoCredito!!, limiteTotalCartaoAtualizado)

            cartaoCredito?.let { viewModel.atualizarCartao(it, limiteTotalCartaoAtualizado) }
        } else {
            if (isDespesaDinheiro) {
                movimentacao.isDespesaDinheiro = true
            } else {
                if (isDespesaPix) {
                    movimentacao.isDespesaPix = true
                }
            }
        }
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

                else -> {}
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.retornaUsuario()
//        viewModel.returnCards()
    }

}