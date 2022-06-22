package com.example.week51

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.*
import java.io.IOException
import java.io.Serializable

const val BASE_URL = "https://api.opendota.com"
const val HERO_STATS = "api/heroStats"

class MainActivity : AppCompatActivity() {
    private lateinit var mHandler: Handler
    private lateinit var client: OkHttpClient
    private lateinit var request: Request
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //создаем объект OkHTTP
        client = OkHttpClient()
        // Объект request с url адречом
        request = Request.Builder()
            .url("$BASE_URL/$HERO_STATS")
            .build()
        //создаем запрс
        getResponse()
        swipeRefreshLayout = findViewById(R.id.sfl_heroes)
        swipeRefreshLayout.setOnRefreshListener {
            getResponse()
        }
    }
    /**
     * Запрос на создание списка из API
     */
    fun getResponse() {
        mHandler = Handler(Looper.getMainLooper())
        //делаем запрос и получаем ответ
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {//если неудачно
                mHandler.post {
                    Toast.makeText(applicationContext, "Нет соеденения!", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout.isRefreshing = false
                }
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {//если удачно
                //обрабатывем прешедщую строку
                val moshi = Moshi.Builder().build()
                //задем ттип списка для полученных даных
                val listType = Types.newParameterizedType(List::class.java, HeroData::class.java)
                //задаем адаптер для списка типа heroData
                val jsonAdapter: JsonAdapter<List<HeroData>> = moshi.adapter(listType)
                //получаем список из полученной json строкуи
                val heroes = response.body?.string()?.let { jsonAdapter.fromJson(it) }

                val listView: ListView? = findViewById(R.id.lv_heroes)
                //адаптер для listview
                val adapter = HeroAdapter(this@MainActivity, heroes)
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
                    swipeRefreshLayout.isRefreshing = false

                    Toast.makeText(applicationContext, "Загруженно из API!", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }
}