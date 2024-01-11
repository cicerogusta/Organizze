package com.cicerodev.yourmoney.data.model

data class User(
    var nome: String = "",
    var email: String = "",
    var senha: String = "",
    val cartoesCredito: MutableList<CartaoCredito> = mutableListOf(),
    var receitaTotal: Double = 0.00,
    var despesaTotal: Double = 0.00

)