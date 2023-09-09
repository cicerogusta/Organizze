package com.example.organizze.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.organizze.R
import com.example.organizze.adapter.MovimentacaoAdapter
import com.example.organizze.base.BaseActivity
import com.example.organizze.data.model.Movimentacao
import com.example.organizze.databinding.ActivityPrincipalBinding
import com.example.organizze.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat


@AndroidEntryPoint
class PrincipalActivity : BaseActivity<PrincipalActivityViewModel, ActivityPrincipalBinding>() {
    override val viewModel: PrincipalActivityViewModel by viewModels()
    private var despesaTotal = 0.0
    private var receitaTotal = 0.0
    private var resumoUsuario = 0.0
    private var mesAnoSelecionado = ""
    private var listaMovimentacoes: MutableList<Movimentacao> = mutableListOf()


    override fun getViewBinding(): ActivityPrincipalBinding =
        ActivityPrincipalBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListener()
        configuraCalendarView()
        binding.toolbar.title = "Organizze"
        setSupportActionBar(binding.toolbar)
        recuperarResumoUsuario()
        recuperarListaMovimentacoes()
    }

    private fun recuperarListaMovimentacoes() {
        viewModel.retornaMovimentacoes(mesAnoSelecionado).observe(this) {
            listaMovimentacoes = it
            configuraRecyclerViewMovimentacao()

        }

        toast(mesAnoSelecionado)
    }

    private fun configuraRecyclerViewMovimentacao() {
        binding.content.recyclerMovimentos.apply {
            layoutManager = LinearLayoutManager(this@PrincipalActivity)
            adapter = MovimentacaoAdapter(listaMovimentacoes, this@PrincipalActivity)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun recuperarResumoUsuario() {
        viewModel.user.observe(this) {

            despesaTotal = it.despesaTotal
            receitaTotal = it.receitaTotal
            resumoUsuario = receitaTotal - despesaTotal

            val decimalFormat = DecimalFormat("0.##")
            val resultadoFormatado = decimalFormat.format(resumoUsuario)

            binding.content.textSaudacao.text = "Olá, ${it.nome}"
            binding.content.textSaldo.text = "R$ $resultadoFormatado"

            if (receitaTotal > despesaTotal) {

                val corReceita = resources.getColor(R.color.colorPrimaryReceita)
                val drawable: Drawable = ColorDrawable(corReceita)

                binding.content.linearLayoutUsuario.background = drawable
            } else {
                val corDespesa = resources.getColor(R.color.colorPrimaryDespesa)
                val drawable: Drawable = ColorDrawable(corDespesa)

                binding.content.linearLayoutUsuario.background = drawable
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSair -> {
                viewModel.sair()
                chamarIntroActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setupClickListener() {
        binding.menuDespesa.setOnClickListener {
            adicionarDespesa()
        }

        binding.menuReceita.setOnClickListener {
            adicionarReceita()
        }
    }

    private fun adicionarDespesa() {
        startActivity(Intent(this, DespesasActivity::class.java))
    }

    private fun adicionarReceita() {
        startActivity(Intent(this, ReceitasActivity::class.java))
    }

    private fun chamarIntroActivity() {
        startActivity(Intent(this, IntroActivity::class.java))
        finish()
    }

    private fun configuraCalendarView() {

        val listaMeses = arrayOf<String>(
            "Janeiro",
            "Fevereiro",
            "Março",
            "Abril",
            "Maio",
            "Junho",
            "Julho",
            "Agosto",
            "Setembro",
            "Outubro",
            "Novembro",
            "Dezembro"
        )

        val calendarView = binding.content.calendarView
        calendarView.setTitleMonths(listaMeses)
        val dataAtual = calendarView.currentDate
        var mesSelecionado = String.format("%02d", (dataAtual.month))
        mesAnoSelecionado = mesSelecionado + "" + dataAtual.year.toString()
        binding.content.calendarView.setOnMonthChangedListener { widget, date ->
            mesSelecionado = String.format("%02d", (date.month))
            mesAnoSelecionado = mesSelecionado + "" + date.year.toString()
            viewModel.removerEventListenerMovimentacao()
            recuperarListaMovimentacoes()
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.recuperarUsuario()
        viewModel.retornaEventListenerUsuario()
        viewModel.retornaEventListenerMovimentacao()
        viewModel.retornaMovimentacoes(mesAnoSelecionado)
    }

    override fun onStop() {
        super.onStop()
        viewModel.removerEventListenerUsuario()
    }

}