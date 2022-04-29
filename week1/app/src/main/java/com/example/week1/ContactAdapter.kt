package com.example.week1

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class ContactAdapter() : BaseAdapter(){
    lateinit var mContacts: ArrayList<Contact>
    lateinit var mContext: Context
    lateinit var mInflater: LayoutInflater

    constructor(Contacts: ArrayList<Contact>, context: Context) : this() {
        mContacts = Contacts
        mContext = context
        mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return mContacts.size
    }

    override fun getItem(position: Int): Any {
        return mContacts[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var nView: View
        nView = mInflater.inflate(R.layout.contact_adapter, parent, false)
        if (mContacts.isEmpty()) return nView

        (nView.findViewById<View>(R.id.tv_name) as TextView).text = mContacts[position].name
        (nView.findViewById<View>(R.id.tv_number) as TextView).text = mContacts[position].number

        return nView
    }
}