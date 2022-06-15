package com.example.week71

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.*
import okhttp3.*
import java.io.*

const val BASE_URL = "https://api.opendota.com"
const val HERO_STATS = "/api/heroStats"

@SuppressLint("SdCardPath")
val PATH = "/data/data/com.example.week71/json/heroes.json"

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var mHandler: Handler
    private lateinit var client: OkHttpClient
    private lateinit var request: Request
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var heroesRepository: HeroesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //создаем объект OkHTTP
        client = OkHttpClient()
        // Объект request с url адречом
        request = Request.Builder()
            .url("$BASE_URL$HERO_STATS")
            .build()
        heroesRepository = HeroesRepository(this)
        GlobalScope.launch {
            heroesRepository.updateHeroesList()
        }

        swipeRefreshLayout = findViewById(R.id.sfl_heroes)
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                getResponse()
            }
        }

        lifecycleScope.launch {
            getResponse()
        }
    }

    /**
     * Делаем запром на создание списка и фала или API
     */
    private suspend fun getResponse() {
        var heroes: List<HeroData>? = mutableListOf()
        lifecycleScope.launch {
            heroes = heroesRepository.getHeroesList()
        }.join()
        if (heroes?.size == 0) {
            return
        }
        val listView: ListView? = findViewById(R.id.lv_heroes)
        //адаптер для listview
        val adapter = HeroAdapter(this@MainActivity, heroes)

        mHandler = Handler(Looper.getMainLooper())
        mHandler.post {
            listView?.adapter = adapter
            listView?.onItemClickListener =//для перехода к подробной статистике о герое
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val intent = Intent(
                        this@MainActivity,
                        HeroStatActivity::class.java
                    )
                    intent.putExtra("HeroData", heroes?.get(position) as Serializable)
                    startActivity(intent)
                }
            adapter.notifyDataSetChanged()
        }
    }

    /**
     * Создаем меню
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Выбор пункта меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_update -> {
                GlobalScope.launch {//обновляем
                    heroesRepository.updateHeroesList()
                }
                return true
            }
            R.id.action_delete -> {//удаляем
                GlobalScope.launch {
                    heroesRepository.deleteFile()
                }
                return true
            }
        }
        return true
    }
}