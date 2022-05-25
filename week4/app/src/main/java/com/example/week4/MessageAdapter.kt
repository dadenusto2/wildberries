package com.example.week4

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//адатер сообщений
class MessageAdapter(private val currentChatList: MutableList<CurrentChatData>, var currentChatRepository: CurrentChatRepository) : RecyclerView
.Adapter<RecyclerView.ViewHolder>() {
    // отображение для моих сообщений
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewMessage: TextView = itemView.findViewById(R.id.tv_message)
        val textViewTime: TextView = itemView.findViewById(R.id.tv_message_time)
        fun setOnclick(currentChat: CurrentChatData, position: Int, currentChatRepository: CurrentChatRepository){
            textViewMessage.text = currentChat.mMessage
            textViewTime.text = currentChat.mTimeSend
            Log.d("---", "asd")
        }
    }

    //отображение для полученных сообщений
    class MyViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewMessage: TextView = itemView.findViewById(R.id.tv_message_out)
        val textViewTime: TextView = itemView.findViewById(R.id.tv_message_time_out)
        fun setOnclick(currentChat: CurrentChatData, position: Int, currentChatRepository: CurrentChatRepository) {
            itemView.isClickable = true

            val currentChatRepository = CurrentChatRepository()
            textViewMessage.text = currentChat.mMessage
            textViewTime.text = currentChat.mTimeSend
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==0) {
            return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.bubble_message, parent, false))
        }
        else{
            return MyViewHolder2(LayoutInflater.from(parent.context)
                .inflate(R.layout.bubble_message_out, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(currentChatList[position].mFromMe) {
            val viewHolder0: MyViewHolder = holder as MyViewHolder
            viewHolder0.setOnclick(currentChatList[position], position, currentChatRepository)
        }
        else{
            val viewHolder2: MyViewHolder2 = holder as MyViewHolder2
            viewHolder2.setOnclick(currentChatList[position], position, currentChatRepository)
        }
    }

    //получаем тип сообщения
    override fun getItemViewType(position: Int): Int {
        return if(currentChatList[position].mFromMe)
            0
        else
            2
    }
    override fun getItemCount() = currentChatList.size
}