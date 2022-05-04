package com.example.week1.Content

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context?) :// класс для взаимодейсвия с бд
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE)// создали таблицу
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")// удалили
        onCreate(sqLiteDatabase)
    }

    companion object {
        private const val DATABASE_NAME = "contacts.db"// название бд
        private const val DATABASE_VERSION = 1// версия бд

        const val TABLE_CONTACTS = "contacts"// название таблицы
        const val CONTACT_ID = "_id"// поле id
        const val CONTACT_NAME = "contactName"// поле имя
        const val CONTACT_PHONE = "contactPhone"// поле номер

        // скрпит создания таблицы
        private const val CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CONTACT_NAME + " TEXT, " +
                CONTACT_PHONE + " TEXT " +
                ")"

        //вывести все данные из таблицы
        val ALL_COLUMNS = arrayOf(CONTACT_ID, CONTACT_NAME, CONTACT_PHONE)
    }
}

