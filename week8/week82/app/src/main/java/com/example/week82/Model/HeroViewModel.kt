package com.example.week82.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Для передаи выбраного героя в фрагмент героя
 */
class HeroViewModel : ViewModel() {
    val heroData = MutableLiveData<HeroData>()

    fun setHeroData(newHeroData: HeroData?) {
        heroData.value = newHeroData
    }
}