package com.example.week81

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.week81.model.HeroData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Фрагмент списка героев
 */
@DelicateCoroutinesApi
class HeroListFragment : Fragment() {
    private lateinit var myFragmentView: View
    private lateinit var mHandler: Handler
    private lateinit var client: OkHttpClient
    private lateinit var request: Request
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

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
        //создаем объект OkHTTP
        client = OkHttpClient()
        // Объект request с url адречом
        request = Request.Builder()
            .url("$BASE_URL$HERO_STATS")
            .build()
        GlobalScope.launch {
            (activity as MainActivity).heroesRepository.updateHeroesList(false)
        }

        swipeRefreshLayout = myFragmentView.findViewById(R.id.sfl_heroes)
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
     * Запрос на создание списка из файла или API
     */
    suspend fun getResponse() {
        var heroes: List<HeroData>? = mutableListOf()
        val job: Job = lifecycleScope.launch {
            heroes = (activity as MainActivity).heroesRepository.getHeroesList()
        }
        job.start()
        job.join()
        if (heroes?.size == 0) {
            swipeRefreshLayout.isRefreshing = false
            return
        }
        val listView: ListView? = myFragmentView.findViewById(R.id.lv_heroes)
        //адаптер для listview
        val adapter = HeroAdapter(this@HeroListFragment, heroes)

        mHandler = Handler(Looper.getMainLooper())
        mHandler.post {
            listView?.adapter = adapter
            listView?.onItemClickListener =//для перехода к подробной статистике о герое
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val activity = activity as MainActivity
                    activity.getCurrentHero(heroes?.get(position))
                }
            adapter.notifyDataSetChanged()
        }
        swipeRefreshLayout.isRefreshing = false
    }
}