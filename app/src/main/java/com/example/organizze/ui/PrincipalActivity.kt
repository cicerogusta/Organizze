package com.example.organizze.ui

import androidx.activity.viewModels
import com.example.organizze.base.BaseActivity
import com.example.organizze.databinding.ActivityPrincipalBinding

class PrincipalActivity : BaseActivity<PrincipalActivityViewModel, ActivityPrincipalBinding>() {
    override val viewModel: PrincipalActivityViewModel by viewModels()

    override fun getViewBinding(): ActivityPrincipalBinding = ActivityPrincipalBinding.inflate(layoutInflater)

    override fun setupClickListener() {

    }

}