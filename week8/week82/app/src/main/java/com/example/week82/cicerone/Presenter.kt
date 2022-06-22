package com.example.week82.cicerone

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Presenter(
    private val router: Router
) {
    init {
        router.navigateTo(Screens.HeroList())
    }
    fun onHeroStat() = router.navigateTo(Screens.HeroStat())
    fun onHeroList() = router.navigateTo(Screens.HeroList())
    fun onAbout() = router.navigateTo(Screens.About())
    fun onBackPressed() {
        router.exit()
    }
}