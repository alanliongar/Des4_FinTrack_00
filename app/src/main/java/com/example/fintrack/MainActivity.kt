package com.example.fintrack
//anotações no final do arquivo mainactivity.kt
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var catss = listOf<CatUiData>()
    private var monyy = listOf<MonyUiData>()
    private val db by lazy {
        //só não escrevo um palavrão auqi pq tem gente lendo meu código
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
        val (dados, categories) = createObjects(applicationContext)
        val rvMony: RecyclerView = findViewById<RecyclerView>(R.id.rv_dados)
        val monyListAdapter = MonyListAdapter()
        rvMony.adapter = monyListAdapter
        val rvCat: RecyclerView = findViewById<RecyclerView>(R.id.rv_category)
        val catListAdapter = CatListAdapter()
        rvCat.adapter = catListAdapter

        GlobalScope.launch(Dispatchers.Main) {
            insertDefaultCat(categories) //inserindo dados padrão
            insertDefaultMony(dados)
            getCategoriesFromDB(catListAdapter) //essa função recebe o adapter e dentro dela a troca de thread acontece pra popular o RV
            getMonyFromDB(monyListAdapter) //mesma coisa aqui
        }
        catListAdapter.setOnClickListener { selected ->
            if (selected.name == "+") {
            val createCategoryBottomSheet = CreateCategoryBottomSheet()
                createCategoryBottomSheet.show(supportFragmentManager,"createCategoryBottomSheet")




            //Snackbar.make(rvCat, "+ is selected", Snackbar.LENGTH_LONG).show()
            } else {
                val catTemp = catss.map { item ->
                    when {
                        item.name == selected.name && !item.isSelected -> item.copy(
                            isSelected = true
                        )

                        item.name == selected.name && item.isSelected -> item.copy(
                            isSelected = false
                        )

                        else -> item
                    }
                } //aqui cattemp é a lista atualizada

                val taskTemp =
                    if (selected.name != "ALL") {
                        monyy.filter { it.category == selected.name }
                    } else {
                        monyy
                    }



                GlobalScope.launch(Dispatchers.Main) {
                    monyListAdapter.submitList(taskTemp)
                    catListAdapter.submitList(catTemp)
                }
            }
        }
        /*
        catListAdapter.setOnLongClickListener { selected ->

        }

        monyListAdapter.setOnClickListener { selected ->

        }

        monyListAdapter.setOnLongClickListener { selected ->

        }
        */
    }

    private suspend fun insertDefaultCat(cats: List<CatUiData>) {
        withContext(Dispatchers.IO) {
            val catEntities = cats.map {
                CatEntity(it.name, it.color, it.isSelected)
            }
            catDao.insertAll(catEntities)
        }
    }

    private suspend fun insertDefaultMony(mony: List<MonyUiData>) {
        withContext(Dispatchers.IO) {
            val monyEntities: List<MonyEntity> = mony.map {
                MonyEntity(id = 0, name = it.name, category = it.category, value = it.value)
            }
            monyDao.insertAll(monyEntities)
        }
    }

    private suspend fun getCategoriesFromDB(catListAdapter: CatListAdapter) {
        withContext(Dispatchers.IO) {
            val categoriesFromDb: List<CatEntity> = catDao.getAll()
            val categoriesFromDbUiData = categoriesFromDb.map {
                CatUiData(name = it.name, color = it.color, isSelected = it.isSelected)
            }.toMutableList()
            categoriesFromDbUiData.add(CatUiData("+", 0, false))

            catss = categoriesFromDbUiData
            withContext(Dispatchers.Main) {
                catListAdapter.submitList(categoriesFromDbUiData)
            }

        }
    }

    private suspend fun getMonyFromDB(monyListAdapter: MonyListAdapter) {
        withContext(Dispatchers.IO) {
            val monyFromDb: List<MonyEntity> = monyDao.getAll()
            val monyFromDbUiData: List<MonyUiData> = monyFromDb.map {
                MonyUiData(name = it.name, category = it.category, value = it.value)
            }
            monyy = monyFromDbUiData
            withContext(Dispatchers.Main) {
                monyListAdapter.submitList(monyFromDbUiData)
            }
        }
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