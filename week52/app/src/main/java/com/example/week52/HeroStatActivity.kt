package com.example.week52

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.week52.Model.HeroData
import kotlin.reflect.full.memberExtensionProperties
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.typeOf

class HeroStatActivity: AppCompatActivity()  {
    lateinit var ivImg : ImageView
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acrivity_hero_characteristics)
        val bundle = intent.extras
        val heroData = bundle?.getSerializable("HeroData") as HeroData

        ivImg = findViewById<ImageView>(R.id.iv_img)

        val layout = findViewById<LinearLayout>(R.id.lv_stat_list)
        val heroName = layoutInflater.inflate(R.layout.hero_charact_item, null) as ViewGroup
        val tvStatName = heroName.findViewById<TextView>(R.id.tv_stat_name)
        val tvStatValue = heroName.findViewById<TextView>(R.id.tv_stat_value)
        tvStatName.text = "Имя"
        tvStatValue.text = heroData.name
        layout.addView(heroName)

        val heroesStats = HeroData::class.memberProperties
        for (stats in heroesStats) {
            if (stats.name != "images" && stats.name != "id" && stats.name != "slug" && stats.name != "name") {
                stats.isAccessible = true
                val curStat = stats.get(heroData)
                val curStatFields = stats.get(heroData)!!.javaClass.declaredFields
                for (stat in curStatFields) {
                    stat.isAccessible = true
                    val statItem =
                        layoutInflater.inflate(R.layout.hero_charact_item, null) as ViewGroup
                    val tvStatName = statItem.findViewById<TextView>(R.id.tv_stat_name)
                    val tvStatValue = statItem.findViewById<TextView>(R.id.tv_stat_value)

                    tvStatName.text = stat.name
                    tvStatValue.text = stat.get(curStat)?.toString()
                    layout.addView(statItem)
                }
            }
        }

        swipeRefreshLayout = findViewById(R.id.sfl_hero_stat)
        swipeRefreshLayout.setOnRefreshListener {
        }
    }
}