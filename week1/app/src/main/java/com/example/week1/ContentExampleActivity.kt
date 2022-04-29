package com.example.week1

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ContentExampleActivity : AppCompatActivity() {

    private lateinit var contact_listview: ListView
    private val colummns = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.HAS_PHONE_NUMBER
    )
    var arrayList = ArrayList<Contact>()
    lateinit var adapter: ContactAdapter
    private lateinit var cursor: Cursor
    lateinit var listview : ListView

    private val contactsListUri: Uri = ContactsContract.Contacts.CONTENT_URI
    private val phoneUri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    private val contactIdUri = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
    private val numberUri = ContactsContract.CommonDataKinds.Phone.NUMBER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_example_activity)
        cursor = contentResolver.query(contactsListUri, null, null, null, null)!!
        readContacts()
    }

    @SuppressLint("Range", "Recycle")
    fun readContacts() = if (cursor.count == 0) {
        Toast.makeText(applicationContext, "No Contacts in Your Directory", Toast.LENGTH_SHORT)
            .show()
    } else {
        var i = 0
           while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(colummns.get(0)))
                val name = cursor.getString(cursor.getColumnIndex(colummns.get(1)))
                val contact = Contact(name, id)
                Log.d("PHONE", name + " " + i.toString())
               if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                   val phones = contentResolver.query(
                       Phone.CONTENT_URI, null,
                       Phone.CONTACT_ID + " = " + id, null, null
                   )
                   if (phones != null) {
                       while (phones.moveToNext()) {
                           val number: String =
                               phones.getString(phones.getColumnIndex(Phone.NUMBER))
                           val type: Int = phones.getInt(phones.getColumnIndex(Phone.TYPE))
                           arrayList.add(Contact(name, number))
                       }
                   }
               }
                else{
                    arrayList.add(Contact(name,"Нет номера"))
                }
           }

        listview = findViewById(R.id.contact_listview)
        adapter = ContactAdapter(arrayList, this)
        listview.adapter = adapter
    }
}