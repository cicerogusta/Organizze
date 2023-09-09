package com.example.organizze.data.model

data class Movimentacao(
    val data: String = "",
    val categoria: String = "",
    val descricao: String = "",
    val tipo: String = "",
    val valor: Double = 0.0
)
