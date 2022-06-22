package com.example.week72

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
import com.example.week72.model.HeroData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.Serializable

const val BASE_URL = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/"

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var heroesRepository: HeroesRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //вызываем retrifit

        heroesRepository = HeroesRepository(this)
        GlobalScope.launch {
            getResponse()
        }

        //задаем обновление на swipe to refresh
        swipeRefreshLayout = findViewById(R.id.sfl_heroes)
        swipeRefreshLayout.setOnRefreshListener {
            GlobalScope.launch {
                getResponse()
            }
        }
    }

    /**
     * Создание меню
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
                GlobalScope.launch {
                    heroesRepository.updateHeroesList()
                    swipeRefreshLayout.isRefreshing = false
                }
                return true
            }
            R.id.action_delete -> {
                GlobalScope.launch {
                    heroesRepository.deleteFile()
                }
                return true
            }
        }
        return true
    }

    /**
     * Запрос на создание списка из Shared Preferences или API
     */
    private suspend fun getResponse() {
        var heroesList: List<HeroData>? = mutableListOf()

        val job: Job = lifecycleScope.launch {
            heroesList = heroesRepository.getHeroesList()
        }
        job.start()
        job.join()
        setList(heroesList)
    }

    /**
     * Отображение списка
     *
     * @param heroesList - список
     */
    fun setList(heroesList: List<HeroData>?) {
        val listView: ListView? = findViewById(R.id.lv_heroes)
        //создаем адаптер
        val adapter = HeroAdapter(this@MainActivity, heroesList)
        val mHandler = Handler(Looper.getMainLooper())
        mHandler.post {
            listView?.adapter = adapter//задаем адаптер
            adapter.notifyDataSetChanged()
            listView?.onItemClickListener =//для запуска экрана с характеристиками героя
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val intent = Intent(
                        this@MainActivity,
                        HeroStatActivity::class.java
                    )
                    val heroData = heroesList as List<HeroData>
                    intent.putExtra("HeroData", heroData[position] as Serializable)
                    startActivity(intent)
                }
        }
    }
}