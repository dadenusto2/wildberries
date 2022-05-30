package com.example.week51

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.load
import kotlin.reflect.full.memberProperties

class HeroStatActivity : AppCompatActivity() {
    lateinit var ivImg : ImageView
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_stat)
        val bundle = intent.extras
        val heroData = bundle?.getSerializable("HeroData") as HeroData

        val heroImgURL = heroData.img

        ivImg = findViewById<ImageView>(R.id.iv_img)
        loadImg(heroImgURL)

        val layout = findViewById<LinearLayout>(R.id.lv_stat_list)
        val heroName = layoutInflater.inflate(R.layout.hero_stat_item, null) as ViewGroup
        val tvStatName = heroName.findViewById<TextView>(R.id.tv_stat_name)
        val tvStatValue = heroName.findViewById<TextView>(R.id.tv_stat_value)
        tvStatName.text = resources.getString(R.string.localizedName)
        tvStatValue.text = heroData.localizedName
        layout.addView(heroName)

        val heroesStats = HeroData::class.memberProperties.sortedBy { !it.isLateinit}
        for (stat in heroesStats) {
            if(stat.name!="img" && stat.name!="icon" && stat.name!="localizedName") {
                val statItem = layoutInflater.inflate(R.layout.hero_stat_item, null) as ViewGroup
                val tvStatName = statItem.findViewById<TextView>(R.id.tv_stat_name)
                val tvStatValue = statItem.findViewById<TextView>(R.id.tv_stat_value)
                val resString: Int = resources.getIdentifier(
                    stat.name,
                    "string",
                    packageName.toString()
                )
                tvStatName.text = resources.getString(resString)
                if(stat.get(heroData).toString() == "null")
                    tvStatValue.text = "Отсутсвует"
                else
                    tvStatValue.text = stat.get(heroData).toString()
                layout.addView(statItem)
            }
        }

        swipeRefreshLayout = findViewById(R.id.sfl_hero_stat)
        swipeRefreshLayout.setOnRefreshListener {
            loadImg(heroImgURL)
        }
        /*val adapter = StatAdapter(heroData, this@HeroStatActivity, heroesStats)
        mHandler = Handler(Looper.getMainLooper());
        mHandler.post {
            listView.adapter = adapter
            adapter.notifyDataSetChanged()
        }*/

    }
    fun loadImg(heroImgURL: String){
        val mHandler: Handler = Handler(Looper.getMainLooper())
        mHandler.post {
            ivImg.load(BASE_URL+ heroImgURL)
            {
                listener(
                    onError = { _, _ ->
                        Toast.makeText(applicationContext, "Нет соеденения!", Toast.LENGTH_LONG)
                            .show()
                        swipeRefreshLayout.isRefreshing = false
                    },
                    onSuccess = { _, _ ->
                        swipeRefreshLayout.isRefreshing = false
                    }
                )
            }
        }
    }
}