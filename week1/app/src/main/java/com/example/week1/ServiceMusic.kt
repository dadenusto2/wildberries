package com.example.week1

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.provider.Settings
import android.widget.Chronometer
import android.widget.Toast
import java.util.*


class ServiceMusic : Service() {
    private lateinit var player: MediaPlayer
    private var time: Long = 0
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private lateinit var timerHandler: Handler
    private lateinit var timerRunnable: Runnable

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Музыка запущена", Toast.LENGTH_SHORT).show()
        player = MediaPlayer.create( this, Settings.System.DEFAULT_RINGTONE_URI )
        player.isLooping
        player.start()
        time = System.currentTimeMillis()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        val totalTime = (System.currentTimeMillis() - time) *0.001
        Toast.makeText(this, "Служба остановлена, время работы: $totalTime сек.", Toast.LENGTH_SHORT).show()
    }

    // Custom method to do a task
    private fun showRandomNumber() {

        mHandler.postDelayed(mRunnable, 5000)
    }
}