package com.cicerodev.yourmoney.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cicerodev.yourmoney.R
import com.cicerodev.yourmoney.adapter.MovimentacaoAdapter
import com.cicerodev.yourmoney.base.BaseActivity
import com.cicerodev.yourmoney.data.model.Movimentacao
import com.cicerodev.yourmoney.databinding.ActivityPrincipalBinding
import com.cicerodev.yourmoney.util.toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import de.timonknispel.ktloadingbutton.KTLoadingButton
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
        binding.toolbar.title = "YourMoney"
        setSupportActionBar(binding.toolbar)
        recuperarResumoUsuario()
        recuperarListaMovimentacoes()
        inicializarAnuncio()

    }

    private fun inicializarAnuncio() {
        MobileAds.initialize(this) {}

        val mAdView = binding.content.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun recuperarListaMovimentacoes() {
        viewModel.retornaMovimentacoes(mesAnoSelecionado).observe(this) {
            listaMovimentacoes = it
            configuraRecyclerViewMovimentacao()


        }
    }

    private fun configuraRecyclerViewMovimentacao() {
        binding.content.recyclerMovimentos.apply {
            layoutManager = LinearLayoutManager(this@PrincipalActivity)
            adapter = MovimentacaoAdapter(
                listaMovimentacoes,
                this@PrincipalActivity
            )
            swipe(this)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun recuperarResumoUsuario() {
        binding.content.textSaudacao.visibility = View.GONE
        binding.content.textSaldo.visibility = View.GONE
        viewModel.user.observe(this) { currentUser ->


            binding.content.testButton.startLoading()
            if (currentUser != null) {


                binding.content.testButton.postDelayed({
                    binding.content.testButton.doResult(true)
                    binding.content.testButton.postDelayed({
                        binding.content.testButton.visibility = View.GONE
                        binding.content.textSaudacao.visibility = View.VISIBLE
                        binding.content.textSaldo.visibility = View.VISIBLE


                    }, 2000)

                }, 2000)






                despesaTotal = currentUser.despesaTotal
                receitaTotal = currentUser.receitaTotal
                resumoUsuario = receitaTotal - despesaTotal

                val decimalFormat = DecimalFormat("0.##")
                val resultadoFormatado = decimalFormat.format(resumoUsuario)

                binding.content.textSaudacao.text = "Olá, ${currentUser.nome}"
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

    private fun swipe(recyclerView: RecyclerView) {
        val itemTouch = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.ACTION_STATE_IDLE
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                excluirMovimentacao(viewHolder)

            }

        }
        ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView)
    }

    private fun atualizarSaldo(position: Int) {
        val movimentacao = listaMovimentacoes[position]
        if (movimentacao.tipo == "r") {
            receitaTotal -= movimentacao.valor
            viewModel.atualizaReceitaTotal(receitaTotal)
        }

        if (movimentacao.tipo == "d") {
            despesaTotal -= movimentacao.valor
            viewModel.atualizaDespesaTotal(despesaTotal)
        }
    }

    private fun excluirMovimentacao(viewHolder: RecyclerView.ViewHolder) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Excluir Movimentação da conta")
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir essa movimentação de sua conta?")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Confirmar") { dialog, which ->
            val position = viewHolder.adapterPosition
            val movimentacao = listaMovimentacoes[position]
            viewModel.removerMovimentacao(mesAnoSelecionado, movimentacao.key)
            binding.content.recyclerMovimentos.adapter?.notifyItemRemoved(position)
            atualizarSaldo(position)
            viewModel.retornaMovimentacoes(mesAnoSelecionado).value?.removeAt(position)

        }
        alertDialog.setNegativeButton("Cancelar") { dialog, which -> toast("Cancelado")}

        val alert = alertDialog.create()
        alert.show()



    }

    override fun onStart() {
        super.onStart()
        viewModel.retornaMovimentacoes(mesAnoSelecionado).value?.clear()
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