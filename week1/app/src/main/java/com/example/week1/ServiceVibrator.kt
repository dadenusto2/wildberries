package com.example.week1

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Vibrator
import android.widget.Toast

class ServiceVibrator : Service() {
    private lateinit var vibrator : Vibrator
    val milliseconds = 1000L

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }
    @SuppressLint("ServiceCast")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Вибрация запущена", Toast.LENGTH_SHORT).show()
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(0, 500, 400, 200)
        vibrator.vibrate(pattern, 2)
        return START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        vibrator.cancel()
        Toast.makeText(this, "Вибрация остановлена", Toast.LENGTH_SHORT).show()
    }
}