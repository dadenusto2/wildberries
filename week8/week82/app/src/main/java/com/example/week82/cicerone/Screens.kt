package com.example.week82.cicerone

import com.example.week82.fragments.AboutFragment
import com.example.week82.fragments.HeroListFragment
import com.example.week82.fragments.HeroStatFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi

object Screens {

    fun HeroList() = FragmentScreen {
        HeroListFragment()
    }

    fun HeroStat() = FragmentScreen {
        HeroStatFragment()
    }

    fun About() = FragmentScreen {
        AboutFragment()
    }
}
