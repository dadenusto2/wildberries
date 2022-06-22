package com.example.week52

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.week52.Model.HeroData
import com.squareup.picasso.Picasso
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class HeroStatActivity : AppCompatActivity() {
    lateinit var ivImg: ImageView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acrivity_hero_characteristics)
        val layout = findViewById<LinearLayout>(R.id.lv_stat_list)
        val heroName = layoutInflater.inflate(R.layout.hero_charact_item, null) as ViewGroup
        val tvHeroName = heroName.findViewById<TextView>(R.id.tv_stat_name)
        val tvHeroNameValue = heroName.findViewById<TextView>(R.id.tv_stat_value)
        ivImg = findViewById(R.id.iv_img)
        swipeRefreshLayout = findViewById(R.id.sfl_hero_stat)
        val bundle = intent.extras
        //получаем данные о герое
        val heroData = bundle?.getSerializable("HeroData") as HeroData

        //задаем фото
        loadImg(heroData)

        //задаем отдельно имя
        tvHeroName.text = "Имя"
        tvHeroNameValue.text = heroData.name
        layout.addView(heroName)//добавляем имя на экран

        //получаем коллекцию, содержащию имя поля и его значение
        val heroDataPref = HeroData::class.memberProperties
        for (pref in heroDataPref) {
            // используем не все поля для вывода
            if (pref.name != "images" && pref.name != "id" && pref.name != "slug" && pref.name != "name") {
                pref.isAccessible = true

                //получаем поля подкласса из HeroData
                val curPref = pref.get(heroData)

                //получаем поля подкласса из HeroData
                val subPrefs = pref.get(heroData)!!.javaClass.declaredFields
                // проходим поля подкласса и выводим их на экран
                for (subPref in subPrefs) {
                    subPref.isAccessible = true
                    val statItem =
                        layoutInflater.inflate(R.layout.hero_charact_item, null) as ViewGroup
                    val tvFieldName = statItem.findViewById<TextView>(R.id.tv_stat_name)
                    val tvValue = statItem.findViewById<TextView>(R.id.tv_stat_value)
                    val resString: Int = resources.getIdentifier(
                        subPref.name,
                        "string",
                        packageName.toString()
                    )
                    tvFieldName.text = resources.getString(resString)
                    // задаем имя поля и его значение
                    tvValue.text = subPref.get(curPref)?.toString()
                    layout.addView(statItem)
                }
            }
        }
        swipeRefreshLayout.setOnRefreshListener {
            loadImg(heroData)
        }
    }

    /**
     * Загрузка изображения через picasso
     *
     * @param heroData - данные героя для загрузки
     */
    fun loadImg(heroData: HeroData) {
        Picasso.with(this)
            .load(heroData.images.lg)
            .into(ivImg)
        swipeRefreshLayout.isRefreshing = false
    }
}