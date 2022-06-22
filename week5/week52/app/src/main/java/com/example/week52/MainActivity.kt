package com.example.week52

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.week52.Model.HeroData
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.Serializable

const val BASE_URL = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/"

/**
 * Список героев
 */
class MainActivity : AppCompatActivity() {
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //вызываем retrifit
        getResponse()

        //задаем обновление на swipe to refresh
        swipeRefreshLayout = findViewById(R.id.sfl_heroes)
        swipeRefreshLayout.setOnRefreshListener {
            getResponse()
        }
    }

    /**
     * Делаем запром на создание списка из Shared Preferences или API
     */
    fun getResponse() {
        //задаем gson для конвертации
        val gson = GsonBuilder()
            .setLenient()
            .create()

        //создаем запрос
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        //указываем класс для класс интерфеса для запрроса
        val service = retrofit.create(RetrofitService::class.java)
        val call: Call<List<HeroData>> = service.heroesData
        //делаем запрос
        call.enqueue(object : Callback<List<HeroData>?> {
            // при получении списока героев передаем их в адаптер для создания списка
            override fun onResponse(
                call: Call<List<HeroData>?>,
                response: Response<List<HeroData>?>
            ) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        setList(response.body())
                        Toast.makeText(this@MainActivity, "Данные из API", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<List<HeroData>?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Нет соединения!", Toast.LENGTH_LONG)
                    .show()
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    /**
     * Задаем списка
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
            swipeRefreshLayout.isRefreshing = false
            listView?.onItemClickListener =//для запуска экрана с характеристиками героя
                AdapterView.OnItemClickListener { parent, view, position, id ->
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

interface RetrofitService {
    //интерфейс для
    @get:GET("all.json")
    val heroesData: Call<List<HeroData>>
}