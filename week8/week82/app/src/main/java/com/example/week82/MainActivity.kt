package com.example.week82

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.week82.cicerone.Presenter
import com.example.week82.fragments.AboutFragment
import com.example.week82.fragments.HeroListFragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val BASE_URL = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/"

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var cicerone: Cicerone<Router>

    /**
     * Возвращает NavigatorHolder
     *
     * @return navigatorHolder
     */
    fun getNavigatorHolder(): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }

    /**
     * Возвращает Router
     *
     * @return Router
     */
    fun getRouter(): Router {
        return cicerone.router
    }

    lateinit var presenter: Presenter

    lateinit var heroesRepository: HeroesRepository

    private val navigator = AppNavigator(this, R.id.main_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //вызываем retrifit
        cicerone = Cicerone.create()
        presenter = Presenter(getRouter())
        //presenter.onHeroList()
        heroesRepository = HeroesRepository(this)
    }

    override fun onResume() {
        super.onResume()
        getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        getNavigatorHolder().removeNavigator()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        getNavigatorHolder().setNavigator(navigator)
    }

    /**
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // если нет фрагмента
        if (supportFragmentManager.findFragmentById(R.id.main_container) == null) {
            finish()
        }
    }

    /**
     * Выбор пункта меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_update -> {
                GlobalScope.launch {
                    if (heroesRepository.updateHeroesList(true)) {
                        val heroListFragment =
                            supportFragmentManager.findFragmentById(R.id.main_container)
                        if (heroListFragment is HeroListFragment) {
                            heroListFragment.getResponse()
                        }
                    }
                }
                return true
            }
            R.id.action_delete -> {
                GlobalScope.launch {
                    heroesRepository.deleteFile()
                }
                return true
            }
            R.id.action_about -> {
                if (supportFragmentManager.findFragmentById(R.id.main_container) !is AboutFragment) {
                    presenter.onAbout()
                }
            }
        }
        return true
    }
}