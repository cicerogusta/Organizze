package com.example.organizze.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.example.organizze.R
import com.example.organizze.base.BaseActivity
import com.example.organizze.databinding.ActivityPrincipalBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrincipalActivity : BaseActivity<PrincipalActivityViewModel, ActivityPrincipalBinding>() {
    override val viewModel: PrincipalActivityViewModel by viewModels()

    override fun getViewBinding(): ActivityPrincipalBinding =
        ActivityPrincipalBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListener()
        configuraCalendarView()
        binding.toolbar.title = "Organizze"
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
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


        binding.content.calendarView.setTitleMonths(listaMeses)
        binding.content.calendarView.setOnMonthChangedListener { widget, date ->  }

    }

}