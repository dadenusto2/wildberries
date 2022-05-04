package com.example.week1.Receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MessageReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {// выполняется при обнаружении
        Toast.makeText(context, "Сообщение от "+ intent.action + ": "+intent.getStringExtra("Message").toString(),
            Toast.LENGTH_LONG).show() // роказываем текст сообщения
    }
}