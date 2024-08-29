package com.example.fintrack
//anotações coloquei num arquivo próprio
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
    private var catsEnt = listOf<CatEntity>()
    private var monyy = listOf<MonyUiData>()
    private val catListAdapter = CatListAdapter()
    val monyListAdapter = MonyListAdapter()
    private val db by lazy {
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
            getCatFromDB() //essa função recebe o adapter e dentro dela a troca de thread acontece pra popular o RV
            getMonyFromDB() //mesma coisa aqui
        }

        catListAdapter.setOnClickListener { selected ->
            if (selected.name == "+") {
                val createCategoryBottomSheet = CreateCategoryBottomSheet() { categoryName ->
                    val catEntity = CatEntity(
                        name = categoryName, color = 50, //ajustar a cor aqui
                        isSelected = false
                    )
                    insertCat(catEntity)
                }
                createCategoryBottomSheet.show(supportFragmentManager, "createCategoryBottomSheet")
                //Snackbar.make(rvCat, "+ is selected", Snackbar.LENGTH_LONG).show()
            } else {
                val catTemp = catss.map { item ->
                    when {
                        item.name == selected.name && item.isSelected -> item.copy(
                            isSelected = true
                        )

                        item.name == selected.name && !item.isSelected -> item.copy(
                            isSelected = true
                        )

                        item.name != selected.name && item.isSelected -> item.copy(
                            isSelected = false
                        )

                        else -> item
                    }
                } //aqui cattemp é a lista atualizada
                GlobalScope.launch(Dispatchers.Main) {
                    filterMonyByCatName(selected.name)
                    catListAdapter.submitList(catTemp)
                }
            }
        }
        monyListAdapter.setOnClickListener { mony ->
            showCreateUpdateMonyBottomSheet(mony)
        }
        catListAdapter.setOnLongClickListener { catToBeDeleted -> //aqui é um catUiData

            if (catToBeDeleted.name != "+" && catToBeDeleted.name != "ALL") {
                val title = this.getString(R.string.delete_cat_title)
                val desc = this.getString(R.string.delete_cat_description)
                val btnText = this.getString(R.string.delete)

                showInfoDialog(title, desc, btnText) {
                    val catEntityToBeDeleted = CatEntity(
                        catToBeDeleted.name, catToBeDeleted.color, catToBeDeleted.isSelected
                    )
                    deleteCat(catEntityToBeDeleted)
                }
            }
        }/*
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

    private fun showInfoDialog(title: String, desc: String, btnText: String, onClick: () -> Unit) {
        val infoBottomSheet = InfoBottomSheet(title, desc, btnText, onClick)
        infoBottomSheet.show(supportFragmentManager, "infoBottomSheet")
    }

    private suspend fun getCatFromDB() {
        withContext(Dispatchers.IO) {
            val categoriesFromDb: List<CatEntity> = catDao.getAll()
            catsEnt = categoriesFromDb
            val categoriesFromDbUiData = categoriesFromDb.map {
                CatUiData(name = it.name, color = it.color, isSelected = it.isSelected)
            }.toMutableList()
            categoriesFromDbUiData.add(CatUiData("+", 0, false))

            val tempCatList = mutableListOf(CatUiData("ALL", 0, false))
            tempCatList.addAll(categoriesFromDbUiData)

            catss = tempCatList
            withContext(Dispatchers.Main) {
                catListAdapter.submitList(catss)
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
                monyListAdapter.submitList(monyy)
            }
        }
    }

    private suspend fun filterMonyByCatName(category: String) {
        var taskTemp: List<MonyUiData> = emptyList()
        withContext(Dispatchers.IO) {
            taskTemp = if (category != "ALL") {
                monyDao.getAllbyCatName(category)
                    .map { MonyUiData(it.id, it.name, it.category, it.value) }
                //monyy.filter { it.category == selected.name }
            } else {
                monyDao.getAll().map { MonyUiData(it.id, it.name, it.category, it.value) }
                //monyy
            }
        }
        withContext(Dispatchers.Main) {
            monyListAdapter.submitList(taskTemp)
        }
    }


    private fun insertCat(catEntity: CatEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            catDao.insert(catEntity)
            getCatFromDB()
        }
    }

    private fun insertMony(monyEntity: MonyEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            monyDao.insert(monyEntity)
            getMonyFromDB()
        }
    }

    private fun updateMony(monyEntity: MonyEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            monyDao.update(monyEntity)
            getMonyFromDB()
        }
    }

    private fun deleteMony(monyEntity: MonyEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            monyDao.delete(monyEntity)
            getMonyFromDB()
        }

    }

    private fun deleteCat(catEntity: CatEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            monyDao.deleteAll(monyDao.getAllbyCatName(catEntity.name))
            catDao.delete(catEntity)
            GlobalScope.launch(Dispatchers.Main) {
                getCatFromDB()
                getMonyFromDB()
            }
        }
    }


    private fun showCreateUpdateMonyBottomSheet(monyUiData: MonyUiData? = null) {
        val createOrUpdateMonyBottomSheet =
            CreateOrUpdateMonyBottomSheet(catsEnt, monyUiData, onCreateClicked = { monyToBeCreated ->
                val monyEntityToBeInsert = MonyEntity(
                    id = 0,
                    name = monyToBeCreated.name,
                    category = monyToBeCreated.category,
                    value = 2.0
                )
                insertMony(monyEntityToBeInsert)
            }, onUpdateClicked = { monyToBeUpdated ->
                val monyyToBeUpdated = MonyEntity(
                    id = monyToBeUpdated.id,
                    name = monyToBeUpdated.name,
                    category = monyToBeUpdated.category,
                    value = monyToBeUpdated.value
                )
                updateMony(monyyToBeUpdated)
            }, onDeleteClicked = { monyToBeDeleted ->
                val monyyToBeDeleted: MonyEntity = MonyEntity(
                    id = monyToBeDeleted.id,
                    name = monyToBeDeleted.name,
                    category = monyToBeDeleted.category,
                    value = monyToBeDeleted.value
                )
                deleteMony(monyyToBeDeleted)
            })
        createOrUpdateMonyBottomSheet.show(supportFragmentManager, "createMonyBottomSheet")
    }
}

fun createObjects(context: Context): Pair<List<MonyUiData>, List<CatUiData>> {
    val dados = listOf(
        MonyUiData(1, "Wifi", "Internet", -130.00),
        MonyUiData(2, "Eletricity bill", "Utilities", -131.00),
        MonyUiData(3, "Gas Station", "Car", -132.00),
        MonyUiData(4, "Water bill", "Utilities", -133.00),
        MonyUiData(5, "Rent", "House", -134.00)
    )
    val categories = listOf(
        CatUiData("Internet", ContextCompat.getColor(context, R.color.violet), false),
        CatUiData("Car", ContextCompat.getColor(context, R.color.red), false),
        CatUiData("Utilities", ContextCompat.getColor(context, R.color.blue), false),
        CatUiData("House", ContextCompat.getColor(context, R.color.green), false)
    )
    return Pair(dados.toList(), categories.toList())
}