package com.example.week1.Content
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.week1.R


class ContactsAdapter() : BaseAdapter() { // адаптер для контактов
    constructor(contact: ArrayList<Contact>, context: Context) : this() {
        mContacts = contact
        mContext = context
        mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
    lateinit var mContacts: ArrayList<Contact> // список контактов
    lateinit var mContext: Context // контекст
    lateinit var mInflater: LayoutInflater // отображение

    override fun getCount(): Int {//кол-во элементов
        return mContacts.size
    }

    override fun getItem(p0: Int): Any {//получить элемент
        return mContacts.get(p0)
    }

    override fun getItemId(p0: Int): Long {// позицияэлемеента
        return p0.toLong()
    }

    // запись в элемент адаптера
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, _convertView: View?, parent: ViewGroup): View {
        var convertView = _convertView
        convertView = mInflater.inflate(R.layout.contact_adapter, parent, false)
        if (mContacts.isEmpty()) return convertView
        // записываем элемент в поля адаптера
        (convertView.findViewById<View>(R.id.tv_name) as TextView).setText(
            mContacts.get(
                position
            ).getName()
        )
        (convertView.findViewById<View>(R.id.tv_number) as TextView).setText(
            mContacts.get(
                position
            ).getNumber()
        )

        return convertView
    }
}