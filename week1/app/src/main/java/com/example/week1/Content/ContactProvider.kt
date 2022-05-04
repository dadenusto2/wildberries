package com.example.week1.Content

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import androidx.annotation.Nullable

// ContentProvider для контактов
class ContactProvider : ContentProvider() {
    companion object {
        private const val AUTHORITY = "com.example.week1" //название поставщика данных
        private const val BASE_PATH = "contacts" //конкретная таблица
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$BASE_PATH") // URI для данных
        private const val CONTACTS = 1
        private const val CONTACT_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH) // для управления

        init {
            uriMatcher.addURI(AUTHORITY, BASE_PATH, CONTACTS)// сслыка для таблицы контактов
            uriMatcher.addURI(AUTHORITY, "$BASE_PATH/#", CONTACT_ID)// сслыка для конктреных контактов
        }
    }

    private lateinit var database: SQLiteDatabase
    override fun onCreate(): Boolean {
        val handler = DBHelper(context)
        database = handler.writableDatabase
        return true
    }

    @Nullable
    //вывод данных
    override fun query(
        uri: Uri, @Nullable strings: Array<String>?, @Nullable s: String?,
        @Nullable strings1: Array<String>?, @Nullable s1: String?
    ): Cursor {
        val cursor: Cursor = when (uriMatcher.match(uri)) {
                CONTACTS -> database.query(
                    DBHelper.TABLE_CONTACTS, DBHelper.ALL_COLUMNS,
                    s, null, null, null, DBHelper.CONTACT_NAME + " ASC"
                )// получаем данные из таблицы в курсор
                else -> throw IllegalArgumentException("Неизвестное URI: $uri")//иначе ошибка
            }
        cursor.setNotificationUri(context!!.contentResolver, uri)// изменения в курсоре
        return cursor
    }

    @Nullable
    override fun getType(uri: Uri): String {//возврат типа
        return when (uriMatcher.match(uri)) {
            CONTACTS -> "com.example.week1/contacts"
            else -> throw IllegalArgumentException("Неизвестное URI: $uri")
        }
    }

    @Nullable
    override fun insert(uri: Uri, @Nullable contentValues: ContentValues?): Uri { // вставка в таблицу
        val id = database.insert(DBHelper.TABLE_CONTACTS, null, contentValues)
        if (id > 0) {
            val _uri = ContentUris.withAppendedId(CONTENT_URI, id)// добавляем id
            context!!.contentResolver.notifyChange(_uri, null)// изменения в курсоре
            return _uri
        }
        throw SQLException("Ошибка вставки в URI: $uri")
    }

    override fun delete(uri: Uri, @Nullable s: String?, @Nullable strings: Array<String>?): Int { // удаление из таблицы
        val delCount =
            when (uriMatcher.match(uri)) {
                CONTACTS -> database.delete(DBHelper.TABLE_CONTACTS, s, strings)
                else -> throw IllegalArgumentException("Ошибка вставки в URI: $uri")
            }
        context!!.contentResolver.notifyChange(uri, null)
        return delCount
    }

    override fun update(// обновление
        uri: Uri, @Nullable contentValues: ContentValues?,
        @Nullable s: String?, @Nullable strings: Array<String>?
    ): Int {
        return 0
    }
}