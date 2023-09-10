package com.cicerodev.yourmoney

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.cicerodev.yourmoney.data.model.User
import com.cicerodev.yourmoney.data.repository.FirebaseRepositoryImp
import com.cicerodev.yourmoney.ui.CadastroActivityViewModel
import com.cicerodev.yourmoney.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class CadastroActivityTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var repository: FirebaseRepositoryImp

    private lateinit var viewModel: CadastroActivityViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        repository = FirebaseRepositoryImp(auth, database)
        viewModel = CadastroActivityViewModel(repository)
    }

    @Test
    fun testeRegistroUsuario() {
        val observer = Observer<UiState<String>> { value ->
            assertEquals(UiState.Success("Registrado com Sucesso"), value)
        }

        // Adicione o Observer ao LiveData
        viewModel.register.observeForever(observer)

        // Acione uma alteração nos dados do LiveData
        viewModel.cadastrarUsuario(User("Teste Unitário", "testeunitario@gmail.com", "12345678"))

        // Remova o Observer após o teste
        viewModel.register.removeObserver(observer)
    }

}
