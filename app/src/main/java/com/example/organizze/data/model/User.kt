package com.example.organizze.data.model

data class User(
    var nome: String = "",
    var email: String = "",
    var senha: String = "",
    var receitaTotal: Double = 0.00,
    var despesaTotal: Double = 0.00

)