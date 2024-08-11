package com.example.fintrack
//anotações no final do arquivo mainactivity.kt
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var catss = listOf<CatUiData>()
    private var monyy = listOf<MonyUiData>()
    private val catListAdapter = CatListAdapter()
    val monyListAdapter = MonyListAdapter()
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
        rvMony.adapter = monyListAdapter
        val rvCat: RecyclerView = findViewById<RecyclerView>(R.id.rv_category)
        rvCat.adapter = catListAdapter
        val fabCreateMony: FloatingActionButton =
            findViewById<FloatingActionButton>(R.id.fab_create_mony)

        fabCreateMony.setOnClickListener {
            showCreateUpdateMonyBottomSheet()
        }


        GlobalScope.launch(Dispatchers.Main) {
            insertDefaultCat(categories) //inserindo dados padrão
            insertDefaultMony(dados)
            getCategoriesFromDB() //essa função recebe o adapter e dentro dela a troca de thread acontece pra popular o RV
            getMonyFromDB() //mesma coisa aqui
        }

        catListAdapter.setOnClickListener { selected ->
            if (selected.name == "+") {
                val createCategoryBottomSheet = CreateCategoryBottomSheet() { categoryName ->
                    val catEntity = CatEntity(
                        name = categoryName,
                        color = 50, //ajustar a cor aqui
                        isSelected = false
                    )
                    insertCat(catEntity)
                }
                createCategoryBottomSheet.show(supportFragmentManager, "createCategoryBottomSheet")
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
        monyListAdapter.setOnClickListener { mony ->
            showCreateUpdateMonyBottomSheet(mony)
        }
        /*        catListAdapter.setOnLongClickListener { selected ->

                }
                monyListAdapter.setOnLongClickListener { selected ->

                }*/
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
                MonyEntity(it.id, name = it.name, category = it.category, value = it.value)
            }
            monyDao.insertAll(monyEntities)
        }
    }

    private suspend fun getCategoriesFromDB() {
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

    private suspend fun getMonyFromDB() {
        withContext(Dispatchers.IO) {
            val monyFromDb: List<MonyEntity> = monyDao.getAll()
            val monyFromDbUiData: List<MonyUiData> = monyFromDb.map {
                MonyUiData(it.id, name = it.name, category = it.category, value = it.value)
            }
            monyy = monyFromDbUiData
            withContext(Dispatchers.Main) {
                monyListAdapter.submitList(monyFromDbUiData)
            }
        }
    }

    private fun insertCat(catEntity: CatEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            catDao.insert(catEntity)
            getCategoriesFromDB()
        }
    }

    private fun insertOrUpdate(monyEntity: MonyEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            monyDao.insertOrUpdate(monyEntity)
            getMonyFromDB()
        }
    }

    private fun showCreateUpdateMonyBottomSheet(monyUiData: MonyUiData? = null) {

        val createOrUpdateMonyBottomSheet =
            CreateOrUpdateMonyBottomSheet(catss, monyUiData, onCreateClicked = { monyToBeCreated ->
                val monyEntityToBeInsert = MonyEntity(
                    id = 0,
                    name = monyToBeCreated.name,
                    category = monyToBeCreated.category,
                    value = 2.0
                )
                insertOrUpdate(monyEntityToBeInsert)
            }, onUpdateClicked = { monyToBeUpdated -> //essa porra aqui é uma uidata
                val monyyToBeUpdated = MonyEntity(
                    id = monyToBeUpdated.id,
                    name = monyToBeUpdated.name,
                    category = monyToBeUpdated.category,
                    value = monyToBeUpdated.value
                )
                println(monyyToBeUpdated.toString() + "Alannnnn")
                insertOrUpdate(monyyToBeUpdated)
            })
        createOrUpdateMonyBottomSheet.show(supportFragmentManager, "createMonyBottomSheet")
    }


}

fun createObjects(context: Context): Pair<List<MonyUiData>, List<CatUiData>> {
    val dados = listOf(
        MonyUiData(0, "Wifi", "Internet", -130.00),
        MonyUiData(0, "Eletricity bill", "Utilities", -131.00),
        MonyUiData(0, "Gas Station", "Car", -132.00),
        MonyUiData(0, "Water bill", "Utilities", -133.00),
        MonyUiData(0, "Rent", "House", -134.00)
    )
    val categories = listOf(
        CatUiData("Internet", ContextCompat.getColor(context, R.color.violet), false),
        CatUiData("Car", ContextCompat.getColor(context, R.color.red), false),
        CatUiData("Utilities", ContextCompat.getColor(context, R.color.blue), false),
        CatUiData("House", ContextCompat.getColor(context, R.color.green), false)
    )
    return Pair(dados.toList(), categories.toList())
}