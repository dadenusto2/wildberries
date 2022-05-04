package com.example.week1

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.week1.Services.ServiceMusic
import com.example.week1.Services.ServiceVibrator

// Service используется для исполнения долгих операций, которые работают в фоновом режиме
// Например, музыкальные плееры
// Пример использования в приложениях: Яндекс.Музыка
class ServiceExampleActivity : AppCompatActivity() {
    var serviceMusic= ServiceMusic::class.java // переменная сервиса музыки
    lateinit var intentMusic : Intent

    var serviceVibrator = ServiceVibrator::class.java// переменная сервиса вибрации
    lateinit var intentVibrator : Intent

    lateinit var musicButton : Button // кнопка для музыка
    lateinit var vibratorButton : Button // кнопка для вибрации\

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_example_activity)
    }

    override fun onResume() {
        super.onResume()

        intentMusic = Intent(this, serviceMusic)

        intentVibrator = Intent(this, serviceVibrator)

        musicButton = findViewById<Button>(R.id.btnServiceMusic)
        vibratorButton = findViewById<Button>(R.id.btnServiceVibro)

        if (isServiceRunning(serviceMusic)) { // состояние сервиса для текста на кнопке
            musicButton.text = getString(R.string.stop_music)
        } else {
            musicButton.text = getString(R.string.start_music)
        }

        musicButton.setOnClickListener {//слушател на кнопку
            if (!isServiceRunning(serviceMusic)) {// если сервес не запущен, запускаем и меняем текст кнопки
                startService(intentMusic)
                musicButton.text = getString(R.string.stop_music)
            } else {// если сервес  запущен, останавливаем и меняем текст кнопки
                stopService(intentMusic)
                musicButton.text = getString(R.string.start_music)
            }
        }

        vibratorButton.setOnClickListener {
            if (!isServiceRunning(serviceVibrator)) {
                startService(intentVibrator)
                vibratorButton.text = getString(R.string.stop_vibro)
            } else {
                stopService(intentVibrator)
                vibratorButton.text = getString(R.string.start_vibro)
            }

        }
    }
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {// проверка состояния сервиса
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
    override fun onPause() {// при остановке останавилваем сервис вибрации, сервис музыка останется в текущем состоянии
        super.onPause()
        if (isServiceRunning(serviceVibrator)) {
            stopService(intentVibrator)
            vibratorButton.text = getString(R.string.start_vibro)
        }
    }
}