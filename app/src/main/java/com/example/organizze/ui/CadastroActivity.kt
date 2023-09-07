package com.example.organizze.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.organizze.base.BaseActivity
import com.example.organizze.databinding.ActivityCadastroBinding

class CadastroActivity : BaseActivity<CadastroActivityViewModel, ActivityCadastroBinding>() {
    override val viewModel: CadastroActivityViewModel by viewModels()
    override fun getViewBinding(): ActivityCadastroBinding =
        ActivityCadastroBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListener()
    }

    override fun setupClickListener() {
        binding.buttonCadastrar.setOnClickListener {
            if (verificaCamposRegistro()) cadastrarUsuario()
        }
    }

    private fun verificaCamposRegistro(): Boolean {
        return if (binding.editNomeRegistro.text.toString().isNotEmpty()) {
             if (binding.editEmailRegistro.text.toString().isNotEmpty()) {
                if (binding.editSenhaRegistro.text.toString().isNotEmpty()) {
                    true
                } else {
                    Toast.makeText(this, "Preencha a senha", Toast.LENGTH_SHORT).show()
                    false
                }
            } else {
                Toast.makeText(this, "Preencha o email!", Toast.LENGTH_SHORT).show()
                false
            }
        } else {
            Toast.makeText(this, "Preencha o nome!", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun cadastrarUsuario() {

    }

}