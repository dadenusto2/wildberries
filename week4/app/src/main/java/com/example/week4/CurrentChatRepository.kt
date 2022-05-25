package com.example.week4

import android.annotation.SuppressLint
import android.util.Log
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

// репозиторий чата
class CurrentChatRepository() : Serializable {
    var messages =//шаблоны сообщений
        arrayOf("Привет!","Как дела"," Все хорошо!"," А ты как"," что-то","Rfgtw","Пока"," Я ушел","Капец"," удачи")

    private var CurrentChatList : MutableList<CurrentChatData> = mutableListOf()

    @SuppressLint("SimpleDateFormat")
    fun generateChats(){//генерация cnfhs[ cjj,otybq
        val sdf = SimpleDateFormat("hh:mm")
        for (i in getSize()..getSize()+10){
            val date  = Date(Date().time - i * 60000) // время меньше на минуту
            CurrentChatList.add(CurrentChatData(i, messages.random(),  sdf.format(date), Random.nextBoolean()))
        }
    }
    @SuppressLint("SimpleDateFormat")
    // добавляем сообщение
    fun addMessage(fromMe:Boolean,  message: String = messages.random()){//добавить сообщение от пользователя
        val sdf = SimpleDateFormat("hh:mm")
        val date = Date()
        CurrentChatList.add(0, CurrentChatData(0, message,  sdf.format(date), true))
    }

    // получаем список сообщений
    fun getCurrentChatList(): MutableList<CurrentChatData>{
        return CurrentChatList
    }

    //задаем новый список сообщений
    fun setNewList(newList: MutableList<CurrentChatData>){
        CurrentChatList = newList.toMutableList()
    }

    //удаляем элемент из списка сообщений
    fun removeItem(position: Int){
        Log.d("size",getCurrentChatList().size.toString())
        CurrentChatList.removeAt(position)
    }

    //элемента на конкретную позицию
    fun addChatBack(position: Int, currentChatData: CurrentChatData){
        CurrentChatList.add(position, currentChatData)
    }

    //конекретный элемент
    fun getCurChat(position: Int): CurrentChatData{
        return CurrentChatList[position]
    }

    // размер
    fun getSize(): Int{
        return CurrentChatList.size
    }
}
