package com.cicerodev.yourmoney.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cicerodev.yourmoney.adapter.CartoesAdapter
import com.cicerodev.yourmoney.base.BaseActivity
import com.cicerodev.yourmoney.data.model.CartaoCredito
import com.cicerodev.yourmoney.databinding.ActivitySeusCartoesBinding
import com.cicerodev.yourmoney.util.toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeusCartoesActivity :
    BaseActivity<SeusCartoesActivityViewModel, ActivitySeusCartoesBinding>() {
    override val viewModel: SeusCartoesActivityViewModel by viewModels()
    private var listaCartoes: MutableList<CartaoCredito> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inicializarAnuncio()
        recuperarListaCartoes()
        setupClickListener()

    }

    private fun recuperarListaCartoes() {
        viewModel.returnCards().observe(this) {
            listaCartoes = it

            configuraRecyclerViewCartoes()
        }

    }

    private fun inicializarAnuncio() {
        MobileAds.initialize(this) {}

        val mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    override fun onStart() {
        super.onStart()
        viewModel.returnUser()
        viewModel.returnCards()
    }

    private fun configuraRecyclerViewCartoes() {
        binding.rvCartoes.apply {
            layoutManager = LinearLayoutManager(this@SeusCartoesActivity)
            adapter = CartoesAdapter(
                listaCartoes,
                viewModel
            )
        }

    }

    override fun getViewBinding(): ActivitySeusCartoesBinding =
        ActivitySeusCartoesBinding.inflate(layoutInflater)

    override fun setupClickListener() {
        binding.buttonAdicionarCartao.setOnClickListener {
            startActivity(Intent(this, CadastraCartaoActivity::class.java))
        }
    }
}