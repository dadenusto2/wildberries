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
class MainActivity : AppCompatActivity() {
    private lateinit var mHandler: Handler
    private lateinit var client: OkHttpClient
    private lateinit var request: Request
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        client = OkHttpClient()
        request = Request.Builder()
            .url("$BASE_URL/api/heroStats")
            .build()
        getResponse()
        swipeRefreshLayout = findViewById(R.id.sfl_heroes)
        swipeRefreshLayout.setOnRefreshListener {
            getResponse()
        }
    }
    fun getResponse(){
        mHandler = Handler(Looper.getMainLooper())
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.post {
                    Toast.makeText(applicationContext, "Нет соеденения!", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout.isRefreshing = false}
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val moshi = Moshi.Builder().build()
                val listType = Types.newParameterizedType(List::class.java, HeroData::class.java)
                val jsonAdapter: JsonAdapter<List<HeroData>> = moshi.adapter(
                    listType
                )
                val heroes = response.body?.string()?.let { jsonAdapter.fromJson(it) }
                val listView: ListView? = findViewById(R.id.lv_heroes)

                val adapter = HeroAdapter(this@MainActivity, heroes)
                mHandler.post {
                    listView?.adapter = adapter
                    listView?.onItemClickListener =
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
                }
            }
        })
    }
}