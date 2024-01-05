package com.cicerodev.yourmoney.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.text.NumberFormat
import java.util.*

class MoneyTextWatcher(editText: EditText) : TextWatcher {

    private val editTextRef: WeakReference<EditText> = WeakReference(editText)
    private val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance()
    private var lastFormattedText: String = ""

    init {
        currencyFormat.maximumFractionDigits = 2
        currencyFormat.currency = Currency.getInstance(Locale.getDefault())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        val editText = editTextRef.get() ?: return
        editText.removeTextChangedListener(this)

        val originalText = editable.toString()

        if (originalText.isNotEmpty()) {
            try {
                val longVal = originalText.replace("[^\\d]".toRegex(), "").toLong()
                val formattedText = currencyFormat.format(longVal / 100.0)

                // Evitar recursão infinita ao definir o texto
                if (formattedText != lastFormattedText) {
                    editText.setText(formattedText)
                    editText.setSelection(formattedText.length)
                    lastFormattedText = formattedText
                }
            } catch (nfe: NumberFormatException) {
                // O texto original não pode ser convertido para um número
                editText.setText("")
            }
        }

        editText.addTextChangedListener(this)
    }
}
