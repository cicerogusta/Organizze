package com.example.organizze.ui

import androidx.lifecycle.ViewModel
import com.example.organizze.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrincipalActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {
}