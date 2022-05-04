package com.example.week1.Services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.provider.Settings
import android.widget.Chronometer
import android.widget.Toast
import java.util.*


class ServiceMusic : Service() {// Service для прослушки музыки
    private lateinit var player: MediaPlayer
    private var time: Long = 0

    override fun onBind(intent: Intent): IBinder {// для подключения из других приложений
        throw UnsupportedOperationException("Not yet implemented")
    }
    // при запуске сервиса
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Музыка запущена", Toast.LENGTH_SHORT).show()
        player = MediaPlayer.create( this, Settings.System.DEFAULT_RINGTONE_URI )// выбираем рингтон по умолчанию
        player.isLooping// зацикливаем
        player.start()// запускаем
        time = System.currentTimeMillis() // для подсчета времени
        return START_STICKY
    }

    override fun onDestroy() {// когда прекращаетися
        super.onDestroy()
        player.stop()// останавливаем player
        val totalTime = (System.currentTimeMillis() - time) *0.001 // считаем время работы сервиса
        Toast.makeText(this, "Служба остановлена, время работы: $totalTime сек.", Toast.LENGTH_SHORT).show()
    }
}