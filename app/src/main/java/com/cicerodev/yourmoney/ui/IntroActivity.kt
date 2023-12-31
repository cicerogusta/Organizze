package com.cicerodev.yourmoney.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.cicerodev.yourmoney.R
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : IntroActivity() {

    private val viewModel: IntroActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configuraSlides()
    }

     fun cadastro(view: View) {
         startActivity(Intent(this, CadastroActivity::class.java))
    }
     fun login(view: View) {
         startActivity(Intent(this, LoginActivity::class.java))

    }

    private fun configuraSlides() {
        isButtonBackVisible = false
        isButtonNextVisible = false
        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .canGoBackward(false)
                .build()
        )

        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        )

        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()
        )

        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build()
        )

        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build()
        )
    }

    override fun onStart() {
        super.onStart()
        if (viewModel.verificaUsuarioAtual()) {
            abrirTelaPrincipal()
        }
    }

    private fun abrirTelaPrincipal() {
        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()
    }
}