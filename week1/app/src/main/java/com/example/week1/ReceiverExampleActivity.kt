package com.example.week1

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class ReceiverExampleActivity : AppCompatActivity() {
    lateinit var localBroadcastManager : LocalBroadcastManager
    var brMessage: MessageReceiver = MessageReceiver()
    var brAirplane: AirplaneReceiver = AirplaneReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receiver_example_activity)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(brMessage)
        unregisterReceiver(brAirplane)
    }

    override fun onResume() {
        super.onResume()

        val ifilter = IntentFilter()
        ifilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(brAirplane, ifilter)

        val ifilter1 = IntentFilter()
        ifilter1.addAction("sendMessage")
        registerReceiver(brMessage, ifilter1)

        localBroadcastManager = LocalBroadcastManager.getInstance(this@ReceiverExampleActivity)
        val ifilter2 = IntentFilter()
        ifilter2.addAction("sendMessage")
        localBroadcastManager.registerReceiver(brMessage, ifilter2)
    }

    fun sendMessage(view: View) {
        Intent().also { intent ->
            intent.action = "sendMessage"
            intent.putExtra("Message", "Сообщение отправлено из ReceiverExampleActivity")
            sendBroadcast(intent)
        }
    }
    fun sendMessageLocal(view: View) {
        Intent().also { intent ->
            intent.action = "sendMessage"
            intent.putExtra("Message", "Локальное сообщение отправлено из ReceiverExampleActivity")
            localBroadcastManager.sendBroadcast(intent)
        }
    }
}