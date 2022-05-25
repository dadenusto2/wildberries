package com.example.week4

import android.os.Message
import java.io.Serializable

//данные чата
class CurrentChatData: Serializable {
    constructor (id :Int, message :String, timeSend : String, fromMe : Boolean) {
        mId = id
        mMessage = message
        mTimeSend = timeSend
        mFromMe = fromMe
    }

    var mId: Int // id
    var mMessage: String//текст сообщения
    var mTimeSend: String//время
    var mFromMe: Boolean//от меня или нет
}