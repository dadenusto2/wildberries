package com.example.week52

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.week52.Model.HeroData
import com.squareup.picasso.Picasso

//адаптер списка героев
class HeroAdapter(private var activity: Activity, private var heroesList: List<HeroData>?) : BaseAdapter() {
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
            this.ivIcon =row?.findViewById(R.id.iv_icon)
        }
    }

    //задаем огтображение списка героев
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
        val curHero = heroesList?.get(p0)

        viewHolder.tvName?.text = curHero!!.name
        Picasso.with(activity)
            .load(curHero.images.xs)
            .into(viewHolder.ivIcon)

        return view as View
    }

}