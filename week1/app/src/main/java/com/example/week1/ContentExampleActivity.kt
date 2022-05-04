package com.example.week1

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.week1.Content.Contact
import com.example.week1.Content.ContactProvider
import com.example.week1.Content.ContactsAdapter
import com.example.week1.Content.DBHelper

// Используется для предоставления доступа к хранилищу данных приложения другим приложениям.
// Пример использования в приложениях: Приложения для контактов, приложения настроек телефона
// пример
// Список контактов(не из контактов телефона), вставка, удаление всех контактов
class ContentExampleActivity : AppCompatActivity(),
    LoaderManager.LoaderCallbacks<Cursor> {// для асинхронной загрузки
        private lateinit var cursorAdapter: ContactsAdapter // адаптер для списка контактов
        lateinit var contacts: ArrayList<Contact>// список контактов
        
        @SuppressLint("Recycle", "Range")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.content_example_activity)
            // add new record - dialog box
            val btn_add = findViewById<View>(R.id.btn_add)//кнопка для добавления контакта
            btn_add.setOnClickListener {
                insertContact("", "", false, false)
            }
            // получаем список данных от поставщика
            val contactUri = contentResolver.query(ContactProvider.CONTENT_URI, null, null, null, null) as Cursor
            contacts = arrayListOf()
            if (contactUri.moveToFirst()) {// добавлем в список
                while(!contactUri.isAfterLast()) {
                    val name = contactUri.getString(contactUri.getColumnIndex(DBHelper.CONTACT_NAME))
                    val phone = contactUri.getString(contactUri.getColumnIndex(DBHelper.CONTACT_PHONE))
                    contacts.add(Contact(name, phone))// добавляем список
                    contactUri.moveToNext()
                }
            }
            Toast.makeText(this, "Кол-во контактов: ${contacts.size}", Toast.LENGTH_LONG).show()
            val list = findViewById<View>(R.id.lv_contacts) as ListView
            cursorAdapter = ContactsAdapter(contacts, this)// добавлем контакты в адаптер
            list.adapter = cursorAdapter
        }

        // передаем строки и было ли пустое поля для ошибки
        fun insertContact(contactName: String, contactPhone: String, emptyName: Boolean, emptyNumber: Boolean)
        {
            val li = LayoutInflater.from(this@ContentExampleActivity)

            // диалоговое окно
            val getEmpIdView: View = li.inflate(R.layout.dialog_box, null)
            val builder: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(this@ContentExampleActivity)
            builder.setTitle("Добавить новый нонтакт")
            builder.setView(getEmpIdView)

            // поля для ввода
            val et_name = getEmpIdView.findViewById<View>(R.id.et_name) as EditText
            val et_number = getEmpIdView.findViewById<View>(R.id.et_number) as EditText

            //если было пустое при отправке, выдаем ошибку
            if (emptyName) et_name.error = "Не введено имя"
            if (emptyNumber) et_number.error = "Не введен номер"

            // если был текст ранее, присваиваем
            et_name.text = Editable.Factory.getInstance().newEditable(contactName)
            et_number.text = Editable.Factory.getInstance().newEditable(contactPhone)
            // set dialog message
            with(builder) {
                setCancelable(true)

                setPositiveButton("Добавить"
                ) { dialog, id -> // get user input and set it to result
                    // если оба поля не пустые
                    if (!et_name.text.toString().isEmpty() && !et_number.text.toString()
                            .isEmpty()
                    ) {
                        // добавляем в поля для отправки
                        val values = ContentValues()
                        values.put(DBHelper.CONTACT_NAME, et_name.text.toString())
                        values.put(DBHelper.CONTACT_PHONE, et_number.text.toString())
                        // insert значений в ContentProvider
                        contentResolver.insert(ContactProvider.CONTENT_URI, values)
                        // добавляем в список
                        contacts.add(Contact(et_name.text.toString(), et_number.text.toString()))
                        // изменяем adapter согласно новому списку
                        cursorAdapter.notifyDataSetChanged()
                    }
                    //иначе вызываем окно с пометкой пустого поля
                    else {
                        insertContact(
                            et_name.text.toString(),
                            et_number.text.toString(),
                            et_name.text.toString().isEmpty(),
                            et_number.text.toString().isEmpty()
                        )
                    }
                    restartLoader()
                }
                setNegativeButton("Отмена", null)
                show()
            }
        }

        // удаляем контакты
        fun deleteAllContacts(view: View) {
            // очищаем список
            contacts.clear()
            // изменяем adapter согласно новому списку
            cursorAdapter.notifyDataSetChanged()
            // удаляем список из content provider
            contentResolver.delete(ContactProvider.CONTENT_URI, null, null)
            restartLoader()
            Toast.makeText(this, "Все контакты удалены", Toast.LENGTH_LONG).show()
        }

        private fun restartLoader() {// дополнительные функции
        }

        override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor?> {
            return CursorLoader(
                this, ContactProvider.CONTENT_URI, null,
                null, null, null
            )
        }

        override fun onLoadFinished(loader: Loader<Cursor?>, data: Cursor?) {
            TODO("Not yet implemented")
        }

        override fun onLoaderReset(loader: Loader<Cursor?>) {
            TODO("Not yet implemented")
        }
    }