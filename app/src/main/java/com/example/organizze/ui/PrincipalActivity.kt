package com.example.organizze.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.organizze.base.BaseActivity
import com.example.organizze.databinding.ActivityPrincipalBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrincipalActivity : BaseActivity<PrincipalActivityViewModel, ActivityPrincipalBinding>() {
    override val viewModel: PrincipalActivityViewModel by viewModels()

    override fun getViewBinding(): ActivityPrincipalBinding = ActivityPrincipalBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListener()
        setSupportActionBar(binding.toolbar)
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

}