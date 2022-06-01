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

        // задаем картинку
        ivImg = findViewById(R.id.iv_img)
        loadImg(heroImgURL)

        val layout = findViewById<LinearLayout>(R.id.lv_stat_list)
        val heroName = layoutInflater.inflate(R.layout.hero_stat_item, null) as ViewGroup
        val tvFieldName = heroName.findViewById<TextView>(R.id.tv_stat_name)
        val tvName = heroName.findViewById<TextView>(R.id.tv_stat_value)
        //задаем имя
        tvFieldName.text = resources.getString(R.string.localizedName)
        tvName.text = heroData.localizedName
        layout.addView(heroName)

        val heroProps = HeroData::class.memberProperties.sortedBy { !it.isLateinit}
        for (curProp in heroProps) {
            if(curProp.name!="img" && curProp.name!="icon" && curProp.name!="localizedName") {
                val statItem = layoutInflater.inflate(R.layout.hero_stat_item, null) as ViewGroup
                val tvFieldName = statItem.findViewById<TextView>(R.id.tv_stat_name)
                val tvValue = statItem.findViewById<TextView>(R.id.tv_stat_value)
                // получаем локализованную строку для названия поля
                val resString: Int = resources.getIdentifier(
                    curProp.name,
                    "string",
                    packageName.toString()
                )
                tvFieldName.text = resources.getString(resString)
                //задаем значение
                if(curProp.get(heroData).toString() == "null")
                    tvValue.text = "Отсутсвует"
                else
                    tvValue.text = curProp.get(heroData).toString()
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