package com.example.week52

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.week52.Model.HeroData
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.Serializable


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val service = retrofit.create(APIService::class.java)
        val call: Call<List<HeroData>> = service.users
        call.enqueue(object : Callback<List<HeroData>?> {

            override fun onResponse(call: Call<List<HeroData>?>, response: Response<List<HeroData>?>) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        val listView: ListView? = findViewById(R.id.lv_heroes)
                        val adapter = HeroAdapter(this@MainActivity, response.body())
                        val mHandler = Handler(Looper.getMainLooper())
                        mHandler.post {
                            listView?.adapter = adapter
                            adapter.notifyDataSetChanged()
                            listView?.onItemClickListener =
                                AdapterView.OnItemClickListener { parent, view, position, id ->
                                    val intent = Intent(
                                        this@MainActivity,
                                        HeroStatActivity::class.java
                                    )
                                    val heroData = response.body() as List<HeroData>
                                    intent.putExtra("HeroData", heroData[position] as Serializable)
                                    startActivity(intent)
                                }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<HeroData>?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Нет соединения!", Toast.LENGTH_LONG)
                    .show()
            }

        })
    }
    interface APIService{
        @get:GET("all.json")
        val users: Call<List<HeroData>>
    }
}
class HeroAdapter(private var activity: Activity, private var listview: List<HeroData>?) : BaseAdapter() {
    override fun getCount(): Int {
        return listview!!.size
    }

    override fun getItem(p0: Int): Any {
        return listview!!.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    private class ViewHolder(row: View?) {
        var tvName: TextView? = null
        var ivIcon: ImageView? = null
        init {
            this.tvName = row?.findViewById(R.id.tv_name)
            this.ivIcon =row?.findViewById(R.id.iv_icon)
        }
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder
        if (p1 == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.hero_item, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = p1
            viewHolder = view.tag as ViewHolder
        }
        val userDto = listview?.get(p0)

        viewHolder.tvName?.text = userDto!!.name
        Picasso.with(activity)
            .load(userDto.images.xs)
            .into(viewHolder.ivIcon)

        return view as View
    }

}