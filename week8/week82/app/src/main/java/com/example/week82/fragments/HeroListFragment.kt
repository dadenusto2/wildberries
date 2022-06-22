package com.example.week82.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.week82.HeroAdapter
import com.example.week82.model.HeroViewModel
import com.example.week82.MainActivity
import com.example.week82.model.HeroData
import com.example.week82.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class HeroListFragment : Fragment() {
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var myFragmentView: View
    private var heroViewModel: HeroViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myFragmentView = inflater.inflate(R.layout.fragment_hero_list, container, false)
        return myFragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        heroViewModel = ViewModelProvider(activity!!).get(HeroViewModel::class.java)
        // = (activity as MainActivity).heroesRepository(activity!!)

        GlobalScope.launch {
            getResponse()
        }

        //задаем обновление на swipe to refresh
        swipeRefreshLayout = myFragmentView.findViewById(R.id.sfl_heroes)
        swipeRefreshLayout.setOnRefreshListener {
            GlobalScope.launch {
                getResponse()
            }
        }
    }

    /**
     * Запрос на создание списка из Shared Preferences или API
     */
    suspend fun getResponse() {
        var heroesList: List<HeroData>? = mutableListOf()

        val job: Job = lifecycleScope.launch {
            heroesList = (activity as MainActivity).heroesRepository.getHeroesList()
        }
        job.start()
        job.join()
        setList(heroesList)
        swipeRefreshLayout.isRefreshing = false
    }

    /**
     * Отображение списка
     *
     * @param heroesList - список
     */
    fun setList(heroesList: List<HeroData>?) {
        val listView: ListView? = myFragmentView.findViewById(R.id.lv_heroes)
        //создаем адаптер
        val adapter = HeroAdapter(this, heroesList)
        val mHandler = Handler(Looper.getMainLooper())
        mHandler.post {
            listView?.adapter = adapter//задаем адаптер
            adapter.notifyDataSetChanged()
            listView?.onItemClickListener =//для запуска экрана с характеристиками героя
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    heroViewModel?.setHeroData(heroesList?.get(position))
                    (activity as MainActivity).presenter.onHeroStat()
                }
        }
    }
}