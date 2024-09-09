package com.example.fintrack

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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
    private lateinit var rvMony: RecyclerView
    private lateinit var ctnEmptyView: LinearLayout
    private lateinit var fabCreateMony: FloatingActionButton
    private lateinit var rvCat: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var ctnTotalView: LinearLayout


    private val db by lazy {
        Room.databaseBuilder(
            applicationContext, FinTrackDataBase::class.java, "samba-lele-oeee-oee-sum"
        ).fallbackToDestructiveMigrationFrom().build()
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
        val (cores, icones) = createLabels(applicationContext)
        rvMony = findViewById(R.id.rv_dados)
        ctnEmptyView = findViewById(R.id.ll_empty_view)
        ctnTotalView = findViewById(R.id.ll_text)
        val monyListAdapter = MonyListAdapter(catDao, this)
        rvMony.adapter = monyListAdapter
        rvCat = findViewById(R.id.rv_category)
        rvCat.adapter = catListAdapter
        fabCreateMony = findViewById(R.id.fab_create_mony)
        val btnCreateEmpty: Button = findViewById<Button>(R.id.btn_create_empty)
        tvTotal = findViewById(R.id.tv_total)
        GlobalScope.launch(Dispatchers.Main) {
            rvMony.isVisible = true
            ctnEmptyView.isVisible = false
            fabCreateMony.isVisible = true
            rvCat.isVisible = true
            ctnTotalView.isVisible = true
        }
        btnCreateEmpty.setOnClickListener {
            showCreateCatBottomSheet(cores, icones)
        }
        fabCreateMony.setOnClickListener {
            showCreateUpdateMonyBottomSheet(null, monyListAdapter)
        }


        GlobalScope.launch(Dispatchers.Main) {
            insertDefaultCat(categories)
            insertDefaultMony(dados)
            getMonyFromDB(monyListAdapter)
            getCatFromDB()
        }

        catListAdapter.setOnClickListener { selected ->
            if (selected.name == "+") {
                showCreateCatBottomSheet(cores, icones)
            } else {
                val catTemp = catss.map { item ->
                    when {
                        item.name == selected.name && item.isSelected -> item.copy(isSelected = true)
                        item.name == selected.name && !item.isSelected -> item.copy(isSelected = true)
                        item.name != selected.name && item.isSelected -> item.copy(isSelected = false)
                        else -> item
                    }
                }

                GlobalScope.launch(Dispatchers.Main) {
                    filterMonyByCatName(selected.name, monyListAdapter)
                    catListAdapter.submitList(catTemp)
                    val valor = getTotalValue(selected.name)
                    tvTotal.text = String.format("$%,.2f", valor)
                    Log.d("Alannn", valor.toString())
                }
            }
        }
        monyListAdapter.setOnClickListener { mony ->
            showCreateUpdateMonyBottomSheet(mony, monyListAdapter)
        }
        catListAdapter.setOnLongClickListener { catToBeDeleted ->
            if (catToBeDeleted.name != "+" && catToBeDeleted.name != "ALL") {
                val title = this.getString(R.string.delete_cat_title)
                val desc = this.getString(R.string.delete_cat_description)
                val btnText = this.getString(R.string.delete)
                showInfoDialog(title, desc, btnText) {
                    val catEntityToBeDeleted = CatEntity(
                        catToBeDeleted.name,
                        catToBeDeleted.color,
                        catToBeDeleted.icon,
                        catToBeDeleted.isSelected
                    )
                    deleteCat(catEntityToBeDeleted, monyListAdapter)
                }
            }
        }
    }

    private suspend fun insertDefaultCat(cats: List<CatUiData>) {
        withContext(Dispatchers.IO) {
            val catEntities = cats.map {
                CatEntity(it.name, it.color, it.icon, it.isSelected)
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
            withContext(Dispatchers.Main) {
                if (catsEnt.isEmpty()) {
                    rvMony.isVisible = false
                    ctnEmptyView.isVisible = true
                    rvCat.isVisible = false
                    fabCreateMony.isVisible = false
                    ctnTotalView.isVisible = false
                } else {
                    rvMony.isVisible = true
                    ctnEmptyView.isVisible = false
                    rvCat.isVisible = true
                    fabCreateMony.isVisible = true
                    ctnTotalView.isVisible = true
                }
            }
            val categoriesFromDbUiData = categoriesFromDb.map {
                CatUiData(
                    name = it.name,
                    color = it.color,
                    icon = it.icon,
                    isSelected = it.isSelected
                )
            }.toMutableList()

            categoriesFromDbUiData.add(
                CatUiData(
                    "+",
                    0,
                    requireNotNull(R.drawable.empty),
                    false
                )
            )
            val tempCatList = mutableListOf<CatUiData>()
            if (monyy.isNotEmpty()) {
                tempCatList.add(
                    CatUiData(
                        "ALL",
                        0,
                        requireNotNull(R.drawable.empty),
                        false
                    )
                )
                ctnTotalView.isVisible = true //tentativa1
            } else {
                withContext(Dispatchers.Main) {
                    ctnTotalView.isVisible = false //tentativa1
                }
            }
            tempCatList.addAll(categoriesFromDbUiData)
            val valor = getTotalValue()
            withContext(Dispatchers.Main) {
                catss = tempCatList
                catListAdapter.submitList(catss)
                tvTotal.text = String.format("$%,.2f", valor)
            }
        }
    }

    private suspend fun getMonyFromDB(monyListAdapter: MonyListAdapter) {
        withContext(Dispatchers.IO) {
            val monyFromDb: List<MonyEntity> = monyDao.getAll()
            val monyFromDbUiData: List<MonyUiData> = monyFromDb.map {
                MonyUiData(it.id, name = it.name, category = it.category, value = it.value)
            }

            withContext(Dispatchers.Main) {
                monyy = monyFromDbUiData
                monyListAdapter.submitList(monyy)
            }
            getCatFromDB()
        }
    }

    private suspend fun filterMonyByCatName(category: String, monyListAdapter: MonyListAdapter) {
        var taskTemp: List<MonyUiData> = emptyList()
        withContext(Dispatchers.IO) {
            taskTemp = if (category != "ALL") {
                monyDao.getAllbyCatName(category)
                    .map { MonyUiData(it.id, it.name, it.category, it.value) }
            } else {
                monyDao.getAll().map { MonyUiData(it.id, it.name, it.category, it.value) }
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

    private fun insertMony(monyEntity: MonyEntity, monyListAdapter: MonyListAdapter) {
        GlobalScope.launch(Dispatchers.IO) {
            monyDao.insert(monyEntity)
            getMonyFromDB(monyListAdapter)
        }
    }

    private fun updateMony(monyEntity: MonyEntity, monyListAdapter: MonyListAdapter) {
        GlobalScope.launch(Dispatchers.IO) {
            monyDao.update(monyEntity)
            getMonyFromDB(monyListAdapter)
        }
    }

    private fun deleteMony(monyEntity: MonyEntity, monyListAdapter: MonyListAdapter) {
        GlobalScope.launch(Dispatchers.IO) {
            monyDao.delete(monyEntity)
            getMonyFromDB(monyListAdapter)
        }
    }

    private fun deleteCat(catEntity: CatEntity, monyListAdapter: MonyListAdapter) {
        GlobalScope.launch(Dispatchers.IO) {
            monyDao.deleteAll(monyDao.getAllbyCatName(catEntity.name))
            catDao.delete(catEntity)
            GlobalScope.launch(Dispatchers.Main) {
                getMonyFromDB(monyListAdapter)
                getCatFromDB()
            }
        }
    }


    private fun showCreateUpdateMonyBottomSheet(
        monyUiData: MonyUiData? = null,
        monyListAdapter: MonyListAdapter
    ) {
        val createOrUpdateMonyBottomSheet = CreateOrUpdateMonyBottomSheet(catsEnt,
            monyUiData,
            onCreateClicked = { monyToBeCreated ->
                val monyEntityToBeInsert = MonyEntity(
                    id = 0,
                    name = monyToBeCreated.name,
                    category = monyToBeCreated.category,
                    value = monyToBeCreated.value
                )
                insertMony(monyEntityToBeInsert, monyListAdapter)
            },
            onUpdateClicked = { monyToBeUpdated ->
                val monyyToBeUpdated = MonyEntity(
                    id = monyToBeUpdated.id,
                    name = monyToBeUpdated.name,
                    category = monyToBeUpdated.category,
                    value = monyToBeUpdated.value
                )
                updateMony(monyyToBeUpdated, monyListAdapter)
            },
            onDeleteClicked = { monyToBeDeleted ->
                val monyyToBeDeleted: MonyEntity = MonyEntity(
                    id = monyToBeDeleted.id,
                    name = monyToBeDeleted.name,
                    category = monyToBeDeleted.category,
                    value = monyToBeDeleted.value
                )
                deleteMony(monyyToBeDeleted, monyListAdapter)
            })
        createOrUpdateMonyBottomSheet.show(supportFragmentManager, "createMonyBottomSheet")
    }

    //verificação
    private fun showCreateCatBottomSheet(cores: List<Cor>, icones: List<Icon>) {
        val createCategoryBottomSheet = CreateCategoryBottomSheet() { CatNew ->
            val catEntity = CatEntity(
                name = CatNew.name,
                color = CatNew.color,
                icon = CatNew.icon,
                isSelected = CatNew.isSelected
            )
            insertCat(catEntity)
        }
        createCategoryBottomSheet.setData(cores, icones)
        createCategoryBottomSheet.show(supportFragmentManager, "createCategoryBottomSheet")
    }

    private suspend fun getTotalValue(category: String? = null): Double {
        var categoria: String? = null
        if (category != "ALL" && category != "+") {
            categoria = category
        }
        val total: Double
        withContext(Dispatchers.IO) {
            total = monyDao.getTotalValue(categoria)
        }
        return total
    }
}

fun createLabels(context: Context): Pair<List<Cor>, List<Icon>> {
    val cores = listOf(
        Cor("white", ContextCompat.getColor(context, R.color.white), false),
        Cor("black", ContextCompat.getColor(context, R.color.black), false),
        Cor("red", ContextCompat.getColor(context, R.color.red), false),
        Cor("pink", ContextCompat.getColor(context, R.color.pink), false),
        Cor("violet", ContextCompat.getColor(context, R.color.violet), false),
        Cor("ocean_blue", ContextCompat.getColor(context, R.color.ocean_blue), false),
        Cor("blue", ContextCompat.getColor(context, R.color.blue), false),
        Cor("water_blue", ContextCompat.getColor(context, R.color.water_blue), false),
        Cor("water_green", ContextCompat.getColor(context, R.color.water_green), false),
        Cor("green", ContextCompat.getColor(context, R.color.green), false),
        Cor("light_yellow", ContextCompat.getColor(context, R.color.light_yellow), false),
        Cor("medium_orange", ContextCompat.getColor(context, R.color.medium_orange), false),
        Cor("brown", ContextCompat.getColor(context, R.color.brown), false),
        Cor("grey", ContextCompat.getColor(context, R.color.grey), false),
    )
    val icones = listOf(
        Icon("camiseta", R.drawable.camiseta, false),
        Icon("carrinho", R.drawable.carrinho, false),
        Icon("carro", R.drawable.carro, false),
        Icon("cartao", R.drawable.cartao, false),
        Icon("casa", R.drawable.casa, false),
        Icon("chave", R.drawable.chave, false),
        Icon("energia", R.drawable.energia, false),
        Icon("gasolina", R.drawable.gasolina, false),
        Icon("gotadagua", R.drawable.gotadagua, false),
        Icon("grafico", R.drawable.grafico, false),
        Icon("joystick", R.drawable.joystick, false),
        Icon("wifi", R.drawable.wifi, false),
    )
    return Pair(cores.toList(), icones.toList())
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
        CatUiData(
            "Internet",
            ContextCompat.getColor(context, R.color.violet),
            requireNotNull(R.drawable.wifi),
            false
        ),
        CatUiData(
            "Car",
            ContextCompat.getColor(context, R.color.red),
            requireNotNull(R.drawable.carro),
            false
        ),
        CatUiData(
            "Utilities",
            ContextCompat.getColor(context, R.color.blue),
            requireNotNull(R.drawable.joystick),
            false
        ),
        CatUiData(
            "House",
            ContextCompat.getColor(context, R.color.green),
            requireNotNull(R.drawable.casa),
            false
        )
    )
    return Pair(dados.toList(), categories.toList())
}