package com.example.week81

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.load
import com.example.week81.model.HeroData
import java.io.Serializable
import kotlin.reflect.full.memberProperties

/**
 * Фрагмент с характеристиками конкретного героя
 */
class HeroStatFragment : Fragment() {
    companion object {
        fun newInstance(heroData: HeroData?): HeroStatFragment {
            val heroStatFragment = HeroStatFragment()
            val args = Bundle()

            args.putSerializable("HeroData", heroData as Serializable)
            heroStatFragment.setArguments(args)
            return heroStatFragment
        }
    }
    lateinit var ivImg: ImageView
    lateinit var heroData: HeroData
    private lateinit var myFragmentView: View
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        heroData = arguments?.getSerializable("HeroData") as HeroData
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        myFragmentView = inflater.inflate(R.layout.fragment_hero_stat, container, false)

        return myFragmentView
    }
    
    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*val bundle = intent.extras
        val heroData = bundle?.getSerializable("HeroData") as HeroData*/

        val heroImgURL = heroData.img

        // задаем картинку
        ivImg = myFragmentView.findViewById(R.id.iv_img)
        loadImg(heroImgURL)

        val layout = myFragmentView.findViewById<LinearLayout>(R.id.lv_stat_list)
        val heroName = layoutInflater.inflate(R.layout.hero_stat_item, null) as ViewGroup
        val tvFieldName = heroName.findViewById<TextView>(R.id.tv_stat_name)
        val tvName = heroName.findViewById<TextView>(R.id.tv_stat_value)
        //задаем имя
        tvFieldName.text = resources.getString(R.string.localizedName)
        tvName.text = heroData.localizedName
        layout.addView(heroName)

        val heroProps = HeroData::class.memberProperties.sortedBy { !it.isLateinit }
        for (curProp in heroProps) {
            if (curProp.name != "img" && curProp.name != "icon" && curProp.name != "localizedName") {
                val statItem = layoutInflater.inflate(R.layout.hero_stat_item, null) as ViewGroup
                val tvFieldName = statItem.findViewById<TextView>(R.id.tv_stat_name)
                val tvValue = statItem.findViewById<TextView>(R.id.tv_stat_value)
                // получаем локализованную строку для названия поля
                val resString: Int = resources.getIdentifier(
                    curProp.name,
                    "string",
                    activity?.packageName.toString()
                )
                tvFieldName.text = resources.getString(resString)
                //задаем значение
                if (curProp.get(heroData).toString() == "null")
                    tvValue.text = "Отсутсвует"
                else
                    tvValue.text = curProp.get(heroData).toString()
                layout.addView(statItem)
            }
        }

        swipeRefreshLayout = myFragmentView.findViewById(R.id.sfl_hero_stat)
        swipeRefreshLayout.setOnRefreshListener {
            loadImg(heroImgURL)
        }

    }

    /**
     * Загрузка избражения
     *
     * @param heroImgURL - сслыка для загрузки
     */
    fun loadImg(heroImgURL: String) {
        val fullPath = BASE_URL + heroImgURL
        ivImg.load(fullPath)
        {
            listener(//если ошибка
                onError = { _, _ ->
                    /*Toast.makeText(applicationContext, "Нет соеденения!", Toast.LENGTH_LONG)
                        .show()*/
                    swipeRefreshLayout.isRefreshing = false
                },
                onSuccess = { _, _ ->
                    swipeRefreshLayout.isRefreshing = false
                }
            )
        }
    }
}