package com.example.fintrack
//primeiro dia: eu criei o item do rv de cada dado, parei na criação do item da rv da categoria (horizontal).
//segundo dia: terminei a parte BEMM BASICA dos layouts, e travei na parte da construção do dado: recuperar a cor pro objeto
//terceiro dia: terminei de tirar as dúvidas relacionadas a criação dos objetos quando precisa do contexto (pra cor), e criei o adapter da monylist.
//quarto dia: implementei somente o adapter da categoria, assim como listei 4 categorias. Falta ajustar os itens para ficarem bonitinhos.
//quinto dia: eu fui criar a bottom sheet, mas me embananei tôdo, preciso revisar o raciocínio do roque e olhar o arquivo do whatsapp
/*
SEXTO DIA: Tentei compilar e deu erro, aí entendi a diferença entre inicializar a variável na prática, e inicializar ela na classe (de clique e clique longo)
Aí decidi que preciso revisitar as aulas e listar o que é feito nelas, pra poder ir aplicando no meu programa
Os proximos passos são: fazer a aplicação completa uma vez, e tentar ir refazendo o projeto algumas vezes, até as aulas se tornarem desnecessárias
o app desde o começo.
*/
//Sétimo dia: depois de procrastinar por 4 dias (vergonha hein?) eu consegui implementar apenas uma entidade e criar o banco de dados do aplicativo. Mas não o declarei nem inicializei na main activity ainda, pq tava revisando as aulas.
//Oitavo dia: Eu terminei a construção bonitinha da parte lógica do banco, mas na hora de implementar na view (o de categoria), não deu pra visualizar. Mandei msg no devspace na dh: 30/07/2024 08:37.


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val db by lazy {
        //pra essa porra parar de reclamar, preciso ficar trocando a versão do banco de dados toda hora nessa caceta
        Room.databaseBuilder(applicationContext, FinTrackDataBase::class.java, "database-fintrack")
            .build()
    }

    private val catDao: CatDao by lazy {
        db.getCatDao()
    }

    private val monyDao by lazy {
        db.getMonyDao()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val (dados, categories) = createObjects(this)
        /*
        * Eu devo incluir aqui a parte do código que transforma as listas em objetos da entidade
        */


        //nesse ponto no código, ambas as listas de objetos estão criadas.
        val rvMony: RecyclerView = findViewById<RecyclerView>(R.id.rv_dados)
        val monyListAdapter = MonyListAdapter()
        rvMony.adapter = monyListAdapter
        //rvMony.layoutManager = LinearLayoutManager(this)
        monyListAdapter.submitList(dados)
        val rvCat: RecyclerView = findViewById<RecyclerView>(R.id.rv_category)
        val catListAdapter = CatListAdapter()
        rvCat.adapter = catListAdapter
        //insertDefaultCat(categories) //inserindo dados padrão
        catListAdapter.submitList(getCategories())
        /*
                catListAdapter.setOnClickListener { selected ->

                }

                catListAdapter.setOnLongClickListener { selected ->

                }

                monyListAdapter.setOnClickListener { selected ->

                }

                monyListAdapter.setOnLongClickListener { selected ->

                }
        */
    }

    private fun insertDefaultCat(cats: List<CatUiData>) {
        val catEntities = cats.map {
            CatEntity(it.name, it.color, it.isSelected)
        }
        GlobalScope.launch(Dispatchers.IO) {
            catDao.insertAll(catEntities)
        }
    }

    fun getCategories(): List<CatUiData> {
        var catUiDatas: List<CatUiData> = emptyList()
        GlobalScope.launch(Dispatchers.IO) {
            val catFromDb: List<CatEntity> = catDao.getAll()
            catUiDatas = catFromDb.map {
                CatUiData(name = it.name, color = it.color, isSelected = it.isSelected)
            }
        }
        return catUiDatas
    }
}

fun createObjects(context: Context): Pair<List<MonyUiData>, List<CatUiData>> {
    val dados = listOf(
        MonyUiData("Wifi", "Internet", -130.00),
        MonyUiData("Eletricity bill", "Utilities", -131.00),
        MonyUiData("Gas Station", "Car", -132.00),
        MonyUiData("Water bill", "Utilities", -133.00),
        MonyUiData("Rent", "House", -134.00)
    )
    val categories = listOf(
        CatUiData("Internet", ContextCompat.getColor(context, R.color.violet), false),
        CatUiData("Car", ContextCompat.getColor(context, R.color.red), false),
        CatUiData("Utilities", ContextCompat.getColor(context, R.color.blue), false),
        CatUiData("House", ContextCompat.getColor(context, R.color.green), false)
    )
    return Pair(dados.toList(), categories.toList())
}