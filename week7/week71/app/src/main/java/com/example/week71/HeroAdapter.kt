package com.example.week71

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.load


/**
 * адаптер для списка героев
 *
 */

class HeroAdapter(private var activity: Activity, private var listview: List<HeroData>?) :
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
            val inflater =
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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