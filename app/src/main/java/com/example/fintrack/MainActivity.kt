package com.example.fintrack
//Primeiro dia: eu criei o item do rv de cada dado, parei na criação do item da rv da categoria (horizontal).
//Segundo dia: terminei a parte BEMM BASICA dos layouts, e travei na parte da construção do dado: recuperar a cor pro objeto
//terceiro dia: terminei de tirar as dúvidas relacionadas a criação dos objetos quando precisa do contexto (pra cor), e criei o adapter da monylist.
//quarto dia: implementei somente o adapter da categoria, assim como listei 4 categorias. Falta ajustar os itens para ficarem bonitinhos.
//
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val (dados, categories) = createObjects(this)
        //nesse ponto no código, ambas as listas de objetos estão criadas.
        val rvMony: RecyclerView = findViewById<RecyclerView>(R.id.rv_dados)
        val monyListAdapter = MonyListAdapter()
        rvMony.adapter = monyListAdapter
        //rvMony.layoutManager = LinearLayoutManager(this)
        monyListAdapter.submitList(dados)
        val rvCat: RecyclerView = findViewById<RecyclerView>(R.id.rv_category)
        val catListAdapter = CatListAdapter()
        rvCat.adapter = catListAdapter
        catListAdapter.submitList(categories)
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