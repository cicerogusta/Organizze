package com.example.organizze.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.example.organizze.base.BaseActivity
import com.example.organizze.databinding.ActivityReceitasBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceitasActivity : BaseActivity<ReceitasActivityViewModel, ActivityReceitasBinding>() {
    override val viewModel: ReceitasActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListener()
    }

    override fun getViewBinding(): ActivityReceitasBinding = ActivityReceitasBinding.inflate(layoutInflater)

    override fun setupClickListener() {

    }

}