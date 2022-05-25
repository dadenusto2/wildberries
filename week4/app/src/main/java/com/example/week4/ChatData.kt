package com.example.week4

class ChatData {//данные чатов
    constructor (chatId :Int, chatImage: Int, chatName : String, currentChatData: CurrentChatRepository) {
        mChatId = chatId//id
        mChatImage = chatImage//изобржение
        mChatName = chatName//имя
        currentChatRepository = currentChatData//репозиторий собщений
    }
    var mChatId : Int
    var mChatImage : Int
    var mChatName : String
    val currentChatRepository : CurrentChatRepository

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}