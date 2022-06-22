package com.example.week81

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import com.example.week81.model.HeroData


/**
 * Адаптер для списка героев
 */
class HeroAdapter(private var activity: Fragment, private var listview: List<HeroData>?) :
    BaseAdapter() {
    override fun getCount(): Int {
        return listview!!.size
    }

    override fun getItem(p0: Int): Any {
        return listview!![p0]
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
            val inflater = activity.layoutInflater
            view = inflater.inflate(R.layout.hero_item, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = p1
            viewHolder = view.tag as ViewHolder
        }
        val heroData = listview?.get(p0)

        viewHolder.tvName?.text = heroData!!.localizedName
        viewHolder.ivIcon?.load(BASE_URL + heroData.icon)//загружаем иконку
        return view as View
    }
}