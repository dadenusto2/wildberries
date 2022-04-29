package com.example.mylists

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.week1.Contact
import com.example.week1.R

class StudentListAdapter(students: ArrayList<Contact>, context: Context) :
    BaseAdapter() {
    lateinit var mContacts: ArrayList<Contact>
    lateinit var context: Context
    lateinit var mInflater: LayoutInflater
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
    override fun getView(position: Int, view: View, parent: ViewGroup): View {
        var view = view
        view = mInflater.inflate(R.layout.contact_adapter, parent, false)
        if (mContacts.isEmpty()) return view
        (view.findViewById<View>(R.id.tv_name) as TextView).setText(mContacts[position].name)
        (view.findViewById<View>(R.id.tv_number) as TextView).setText(mContacts[position].number)
        return view
    }
}