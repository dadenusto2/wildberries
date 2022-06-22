package com.example.week81

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.week81.model.HeroData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


const val BASE_URL = "https://api.opendota.com"
const val HERO_STATS = "/api/heroStats"

@SuppressLint("SdCardPath")
val PATH = "/data/data/com.example.week81/json/heroes.json"

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    lateinit var heroesRepository: HeroesRepository
    private lateinit var heroListFragment: HeroListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        // задаем фрагмент списка героев
        heroListFragment = HeroListFragment()
        ft.add(R.id.frame_layout, heroListFragment, "list")
        ft.commit()
        heroesRepository = HeroesRepository(this)
    }

    /**
     * Переход к статисике конкретного героя
     *
     * @param heroData - конкретный герой
     */
    fun getCurrentHero(heroData: HeroData?) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val heroStatFragment = HeroStatFragment.newInstance(heroData)
        ft.replace(R.id.frame_layout, heroStatFragment)
        ft.addToBackStack("list")
        ft.commit()
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
                GlobalScope.launch {//обновляем
                    if (heroesRepository.updateHeroesList(true))
                        heroListFragment.getResponse()
                }
                return true
            }
            R.id.action_delete -> {//удаляем
                GlobalScope.launch {
                    heroesRepository.deleteFile()
                }
                return true
            }
            R.id.action_about -> {//"о приложении"
                if (supportFragmentManager.findFragmentById(R.id.frame_layout) !is AboutFragment) {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    val aboutFragment = AboutFragment()
                    ft.addToBackStack(null)
                    ft.replace(R.id.frame_layout, aboutFragment)
                    ft.commit()
                }
            }
        }
        return true
    }
}