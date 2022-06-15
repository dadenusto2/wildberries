package com.example.week72

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.week72.Model.HeroData
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi


val PATH_ICON = "/data/data/com.example.week72/images/icons/"

//адаптер списка героев
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

    //задаем огтображение списка героев
    @OptIn(DelicateCoroutinesApi::class)
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

        getImageView(viewHolder.ivIcon, curHero)

        return view as View
    }

    fun getImageView(imageView: ImageView?, curHero: HeroData?) {

        Picasso.with(activity)
            .load(curHero?.images?.xs)
            .into(imageView)
        /*val imageRepository = ImageRepository()
        val fullPath = curHero?.images?.xs
        val fullName = fullPath?.substringAfterLast("/")
        val pathToIcon = imageRepository.readImageFromPref(activity,
            "icon_$fullName")

        val file = File(pathToIcon)

        if (file.isFile) {
            Picasso.with(activity)
                .load(curHero?.images?.xs)
                .into(imageView)
            Log.d("---", "icon_$fullName from file")
        } else {
            //GlobalScope.launch {
                imageRepository.saveIcon(activity,
                    curHero,
                    "icon_$fullName",
                    PATH_ICON)
            //}

            Log.d("---","${PATH_ICON}icon_$fullName")
            Picasso.with(activity)
                .load(fullPath)
                .into(imageView)
            Log.d("---", "icon_$fullName from API")
        }*/
    }
}