package com.example.week4

class ChatsRepository (){
    var names =
        arrayOf("Даниил","Максим"," Владислав"," Никита"," Артем"," Иван"," Кирилл"," Егор"," Илья"," Андрей")
    var surname =
        arrayOf("Иванов"," Смирнов"," Кузнецов"," Попов"," Васильев"," Петров"," Соколов"," Лазарев"," Медведев"," Ершов")
    var images =
        arrayOf(R.drawable.ic_android_black_24dp, R.drawable.ic_android_red_24dp, R.drawable.ic_android_blue_24dp, R.drawable.ic_android_green_24dp)

    lateinit var currentChatRepository: CurrentChatRepository

    private var ChatList : MutableList<ChatData> = mutableListOf()
    var index : Int = 0
    fun generateChats(){//генерируем предыдущие саты
        for (i in index+1..index+10){
            currentChatRepository = CurrentChatRepository()
            currentChatRepository.generateChats()
            ChatList.add(ChatData(i,images.random(),  names.random() + " " + surname.random(), currentChatRepository))
            index = i
        }
    }
    //получитьчписок чатов
    fun getChats(): MutableList<ChatData>{
        return ChatList
    }
    //добавить чат но позицию
    fun setChatList(newChatData :ChatData, position: Int){
        ChatList.add(position, newChatData)
    }
    //размер
    fun getChatsSize(): Int{
        return ChatList.size
    }
    //удалить
    fun delFromChats(index: Int){
        ChatList.removeAt(index)
    }

    fun getChatItem(position: Int = 0): ChatData{
        return ChatList[position]
    }

    // задать новый список чатов
    fun setList(newChatList : MutableList<ChatData>){
        var newCurChatList = mutableListOf<ChatData>()
        for(i:Int in 0 until newChatList.size){
            val newChatRepository = CurrentChatRepository()
            newChatRepository.setNewList(newChatList[i].currentChatRepository.getCurrentChatList())

            val newChatData = ChatData(newChatList[i].mChatId, newChatList[i].mChatImage,
                                        newChatList[i].mChatName, newChatRepository)
            newCurChatList.add(newChatData)
        }
        ChatList = newCurChatList.toMutableList()
    }
}