package com.example.week1.Services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Vibrator
import android.widget.Toast

class ServiceVibrator : Service() {// Service для прослушки вибрации
    private lateinit var vibrator : Vibrator

    override fun onBind(intent: Intent): IBinder {// для подключения из других приложений
        throw UnsupportedOperationException("Not yet implemented")
    }
    // при запуске сервиса
    @SuppressLint("ServiceCast")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Вибрация запущена", Toast.LENGTH_SHORT).show()
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator // получаем доступ к сервису
        val pattern = longArrayOf(0, 500, 400, 200)//патерн вибрации
        vibrator.vibrate(pattern, 2)
        return START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        vibrator.cancel()// остановка вибрации
        Toast.makeText(this, "Вибрация остановлена", Toast.LENGTH_SHORT).show()
    }
}