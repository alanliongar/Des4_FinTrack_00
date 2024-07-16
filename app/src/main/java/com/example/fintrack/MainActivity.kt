package com.example.fintrack
//Primeiro dia: eu criei o item do rv de cada dado, parei na criação do item da rv da categoria (horizontal).
//Segundo dia: terminei a parte BEMM BASICA dos layouts, e travei na parte da construção do dado: recuperar a cor pro objeto

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}


val dados = listOf(
    mony_data("Wifi", "Internet", -130.00),
    mony_data("Eletricity bill", "Utilities", -131.00),
    mony_data("Gas Station", "Car", -132.00),
    mony_data("Water bill", "Utilities", -133.00),
    mony_data("Rent", "House", -134.00)
)

val categories = listOf(
    cat_data("Internet", ResourcesCompat.getColor(getResources(),R.color.red,null), false)
)