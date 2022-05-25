package com.example.week4

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//чаты без адаптеров
class ChatsWithoutListActivity: AppCompatActivity() {
    lateinit var chatsRepository: ChatsRepository

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chats_withou_list)

        //задаем чрепозиторий чатов и список чатов
        chatsRepository = ChatsRepository()
        chatsRepository.generateChats()
        val chatsList = chatsRepository.getChats()
        // отображение чата
        val layout = findViewById<LinearLayout>(R.id.lv_chat_without_list)
        layout.orientation = LinearLayout.VERTICAL
        for (i in 0 until chatsList.size) {
            // получаем элемент чата
            val chatItem = layoutInflater.inflate(R.layout.recyclerview_item, null) as ViewGroup

            //задаем значение
            val ivImage = chatItem.findViewById<ImageView>(R.id.iv_image)
            val tvName = chatItem.findViewById<TextView>(R.id.tv_name)
            val tvMessage = chatItem.findViewById<TextView>(R.id.tv_first_message)
            tvName.text = chatsList[i].mChatName
            tvMessage.text = chatsList[i].currentChatRepository.getCurChat(0).mMessage
            ivImage.setImageResource(chatsList[i].mChatImage)

            //добвляем на отображение
            layout.addView(chatItem)
        }
        repeater()//новые ссобщения
    }
    fun repeater() {
        val chatsList = chatsRepository.getChats()
        lifecycleScope.launch { ->
            repeat(100) {
                // выбираем чат для нового сообщния
                val position : Int = (0 until chatsList.size).random()
                delay((1000..2000).random().toLong())//бэкенд

                //добавлем новое сообщение и чат первый
                chatsRepository.getChatItem(position).currentChatRepository.addMessage(false)
                val tmp = chatsRepository.getChatItem(position)
                chatsRepository.delFromChats(position)
                chatsRepository.setChatList(tmp, 0)

                //новое отображение
                val chatItem = layoutInflater.inflate(R.layout.recyclerview_item, null) as ViewGroup

                val ivImage = chatItem.findViewById<ImageView>(R.id.iv_image)
                val tvName = chatItem.findViewById<TextView>(R.id.tv_name)
                val tvMessage = chatItem.findViewById<TextView>(R.id.tv_first_message)
                tvName.text = chatsList[position].mChatName
                tvMessage.text = chatsList[position].currentChatRepository.getCurChat(0).mMessage
                ivImage.setImageResource(chatsList[position].mChatImage)

                //удаляем старый и задаем новый вверху
                val layout = findViewById<LinearLayout>(R.id.lv_chat_without_list)
                layout.orientation = LinearLayout.VERTICAL
                layout.removeView(layout.getChildAt(position))
                layout.addView(chatItem, 0)
            }
        }
    }
}