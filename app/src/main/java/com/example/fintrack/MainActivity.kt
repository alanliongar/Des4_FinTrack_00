package com.example.fintrack
//Primeiro dia: eu criei o item do rv de cada dado, parei na criação do item da rv da categoria (horizontal).
//Segundo dia: terminei a parte BEMM BASICA dos layouts, e travei na parte da construção do dado: recuperar a cor pro objeto
//terceiro dia: terminei de tirar as dúvidas relacionadas a criação dos objetos quando precisa do contexto (pra cor), e criei o adapter da monylist.
//
//
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val (dados, categories) = createObjects(this)
        //nesse ponto no código, ambas as listas de objetos estão criadas.
    }
}

fun createObjects(context: Context): Pair<List<MonyData>, List<CatData>> {
    val dados = listOf(
        MonyData("Wifi", "Internet", -130.00),
        MonyData("Eletricity bill", "Utilities", -131.00),
        MonyData("Gas Station", "Car", -132.00),
        MonyData("Water bill", "Utilities", -133.00),
        MonyData("Rent", "House", -134.00)
    )
    val categories = listOf(
        CatData("Internet", ContextCompat.getColor(context, R.color.violet), false),
        CatData("Car", ContextCompat.getColor(context, R.color.red), false),
        CatData("Utilities", ContextCompat.getColor(context, R.color.blue), false),
        CatData("House", ContextCompat.getColor(context, R.color.green), false)
    )
    return Pair(dados.toList(), categories.toList())
}