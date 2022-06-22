package com.example.week72

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.week72.model.HeroData
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi

/**
 * Адаптер списка героев
 */
@DelicateCoroutinesApi
class HeroAdapter(private var activity: MainActivity, private var heroesList: List<HeroData>?) :
    BaseAdapter() {

    override fun getCount(): Int {
        return heroesList!!.size
    }

    override fun getItem(p0: Int): Any {
        return heroesList!!.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    private class ViewHolder(row: View?) {
        var tvName: TextView? = null
        var ivIcon: ImageView? = null

        init {
            this.tvName = row?.findViewById(R.id.tv_name)
            this.ivIcon = row?.findViewById(R.id.iv_icon)
        }
    }

    @SuppressLint("InflateParams")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder
        if (p1 == null) {
            val inflater =
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.hero_item, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = p1
            viewHolder = view.tag as ViewHolder
        }
        val curHero = heroesList?.get(p0)

        viewHolder.tvName?.text = curHero!!.name

        setImageView(viewHolder.ivIcon, curHero)

        return view as View
    }

    /**
     * Загрузка изображения через picasso
     *
     * @param imageView - изображение
     * @param curHero - данные героя для загрузки
     */
    fun setImageView(imageView: ImageView?, curHero: HeroData?) {
        Picasso.with(activity)
            .load(curHero?.images?.xs)
            .into(imageView)
    }
}