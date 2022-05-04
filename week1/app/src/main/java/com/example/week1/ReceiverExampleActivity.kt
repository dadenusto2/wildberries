package com.example.week1

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.week1.Receivers.AirplaneReceiver
import com.example.week1.Receivers.MessageReceiver

// Broadcast Receiver
// Предназначен для получения различных сообщений от системы или других приложений
// Например, может применятся в приложениях, требющих постоянное подключение к интернету
// Пример использования в приложениях: Chrome, яндекс.клавиатура, различные лаунчеры
// пример 1
// При включени режима в самолете появлется AlertDialog с предложением выключить его через настройки(получение сообщений)
// пример 2
// Отправка соообщений всей системе по нажатию кнопки
// пример 3
// Отправка соообщений всей локально(лучше для защиты данных)
// пример 4
// Получение сообщений через объявленый в манифесте Receiver() при изменении часового пояса
class ReceiverExampleActivity : AppCompatActivity() {
    lateinit var brMessage: MessageReceiver // receiver для прослушивания сообщений
    lateinit var brAirplane: AirplaneReceiver // receiver для обнаружения включения режима в самолете
    lateinit var localBroadcastManager : LocalBroadcastManager // рассылка для сообщений внутри приложения

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receiver_example_activity)
    }

    override fun onResume() {
        super.onResume()

        brMessage = MessageReceiver() // объект необходимого ресивера
        val ifilterMessageReceiver = IntentFilter() // какие намерения приложение должно обрабатывать
        ifilterMessageReceiver.addAction("sendMessage")// название намерения
        registerReceiver(brMessage, ifilterMessageReceiver) // регестрируем receiver

        brAirplane = AirplaneReceiver()
        val ifilterAirplaneReceiver = IntentFilter()
        ifilterAirplaneReceiver.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(brAirplane, ifilterAirplaneReceiver)

        // отдельный тип для локальных сообщений
        localBroadcastManager = LocalBroadcastManager.getInstance(this@ReceiverExampleActivity)
        val ifilterLocalBroadcastManager = IntentFilter()
        ifilterLocalBroadcastManager.addAction("sendMessageLocal")
        localBroadcastManager.registerReceiver(brMessage, ifilterLocalBroadcastManager)
    }

    override fun onPause() {
        super.onPause()
        // снимаем регестрацию
        unregisterReceiver(brMessage)
        unregisterReceiver(brAirplane)
        localBroadcastManager.unregisterReceiver(brMessage)
    }

    fun sendMessage(view: View) {// отправка сообщения системе
        Intent().also { intent ->
            intent.action = "sendMessage"//название намерения
            intent.putExtra("Message", "Сообщение отправлено из ReceiverExampleActivity") // сообщение
            sendBroadcast(intent)//отправка
        }
    }
    fun sendMessageLocal(view: View) {// отправка сообщения внутри приложения
        Intent().also { intent ->
            intent.action = "sendMessageLocal"
            intent.putExtra("Message", "Локальное сообщение отправлено из ReceiverExampleActivity")
            localBroadcastManager.sendBroadcast(intent)
        }
    }
}