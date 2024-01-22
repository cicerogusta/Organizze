package com.cicerodev.yourmoney.util

import android.util.Base64
import java.text.SimpleDateFormat

fun codificarBase64(texto: String): String {
    return Base64.encodeToString(texto.toByteArray(), Base64.DEFAULT)
        .replace("(\\n|\\r)".toRegex(), "")
}

fun decodificarBase64(textoCodificado: String?): String {
    return String(Base64.decode(textoCodificado, Base64.DEFAULT))
}

fun removePoints(input: String): String {
    return input.replace(".", "").replace("", "").replace("R$", "").replace(",", ".").trim()
}

fun formataDataCartao(data: String): String {
    val dataOriginal = data
    var dataFormatada = ""
    if (dataOriginal.length == 4) {
        val dia = dataOriginal.substring(0, 2)
        val mes = dataOriginal.substring(2)

         dataFormatada = "$dia/$mes"

        println("Data formatada: $dataFormatada")
    } else {
        println("A string não possui o formato esperado.")
    }
    return dataFormatada
}

fun dataAtual(): String? {
    val data = System.currentTimeMillis()
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    return simpleDateFormat.format(data)
}

fun formataData(data: String): String {
    val retornoData = data.split("/")
    val dia = retornoData[0]
    val mes = retornoData[1]
    val ano = retornoData[2]

    return mes + ano
}