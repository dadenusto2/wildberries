package com.example.week71

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.Serializable

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
            heroesRepository.updateHeroesList(false)
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
     * Запром на создание списка из файла или API
     */
    private suspend fun getResponse() {
        val heroes = heroesRepository.getHeroesList()
        if (heroes?.size == 0) {
            swipeRefreshLayout.isRefreshing = false
            return
        }
        val listView: ListView? = findViewById(R.id.lv_heroes)
        //адаптер для listview
        val adapter = HeroAdapter(this@MainActivity, heroes)

        mHandler = Handler(Looper.getMainLooper())
        mHandler.post {
            listView?.adapter = adapter
            listView?.onItemClickListener =//для перехода к подробной статистике о герое
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val intent = Intent(
                        this@MainActivity,
                        HeroStatActivity::class.java
                    )
                    intent.putExtra("HeroData", heroes?.get(position) as Serializable)
                    startActivity(intent)
                }
            adapter.notifyDataSetChanged()
        }
        swipeRefreshLayout.isRefreshing = false
    }

    /**
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R. /**
         * Выбор пункта меню
         */menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_update -> {
                GlobalScope.launch {//обновляем
                    if(heroesRepository.updateHeroesList(true))
                        getResponse()
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