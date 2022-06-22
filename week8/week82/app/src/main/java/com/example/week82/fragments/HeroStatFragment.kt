package com.example.week82.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.week82.model.HeroViewModel
import com.example.week82.model.HeroData
import com.example.week82.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@DelicateCoroutinesApi
class HeroStatFragment : Fragment() {
    lateinit var ivImg: ImageView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var myFragmentView: View

    override fun onCreateView(
        inflater: LayoutInflater,

        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myFragmentView = inflater.inflate(R.layout.fragment_hero_stat, container, false)

        return myFragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layout = myFragmentView.findViewById<LinearLayout>(R.id.lv_stat_list)

        val heroName = layoutInflater.inflate(R.layout.hero_charact_item, null) as ViewGroup

        val tvHeroName = heroName.findViewById<TextView>(R.id.tv_stat_name)
        val tvHeroNameValue = heroName.findViewById<TextView>(R.id.tv_stat_value)

        ivImg = myFragmentView.findViewById(R.id.iv_img)
        swipeRefreshLayout = myFragmentView.findViewById(R.id.sfl_hero_stat)
        //val bundle = intent.extras
        //получаем данные о герое

        var heroData = HeroData()
        val model = ViewModelProvider(activity!!).get(HeroViewModel::class.java)
        model.heroData.observe(activity!!
        ) { o -> heroData = o as HeroData }
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
            if (pref.name != "images" && pref.name != "id"
                && pref.name != "slug" && pref.name != "name"
            ) {
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
                        activity?.packageName.toString()
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
        Picasso.with(activity)
            .load(heroData.images.lg)
            .into(ivImg)

        swipeRefreshLayout.isRefreshing = false
    }
}