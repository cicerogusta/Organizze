package com.cicerodev.yourmoney.ui

import androidx.lifecycle.ViewModel
import com.cicerodev.yourmoney.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroActivityViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {
        fun verificaUsuarioAtual(): Boolean {
            return repository.isCurrentUser()
        }
}