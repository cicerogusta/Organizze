package com.cicerodev.yourmoney.data.model

data class Movimentacao(
    val data: String = "",
    val categoria: String = "",
    val descricao: String = "",
    val tipo: String = "",
    val valor: Double = 0.0,
    var key: String = "",
    var isDespesaCartao: Boolean = false,
    var isReceitaCartao: Boolean = false,
    var isDespesaPix: Boolean = false,
    var isReceitaPix: Boolean = false,
    var isDespesaDinheiro: Boolean = false,
    var isReceitaDinheiro: Boolean = false,
    var cartaoCredito: CartaoCredito = CartaoCredito()
)
