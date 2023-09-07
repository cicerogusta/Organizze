package com.example.organizze.util

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


fun AppCompatActivity.toast(msg: String?){
    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
