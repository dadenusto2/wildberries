package com.example.week1

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ServiceExampleActivity : AppCompatActivity() {
    var serviceMusic= ServiceMusic::class.java
    lateinit var intentMusic : Intent

    var serviceVibrator = ServiceVibrator::class.java
    lateinit var intentVibrator : Intent

    lateinit var musicButton : Button
    lateinit var vibratorButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_example_activity)

        intentMusic = Intent(this, serviceMusic)

        intentVibrator = Intent(this, serviceVibrator)

        musicButton = findViewById<Button>(R.id.btnServiceMusic)
        vibratorButton = findViewById<Button>(R.id.btnServiceStop)


        musicButton.setOnClickListener {
            // If the service is not running then start it
            if (!isServiceRunning(serviceMusic)) {
                startService(intentMusic)
                musicButton.text = "Остановить музыку"
            } else {
                stopService(intentMusic)
                musicButton.text = "Запустить музыку"
            }
        }

        vibratorButton.setOnClickListener {
        if (!isServiceRunning(serviceVibrator)) {
            startService(intentVibrator)
            vibratorButton.text = "Остановить вибрацию"
        } else {
            stopService(intentVibrator)
            vibratorButton.text = "Запустить вибрацию"
        }

        }
    }
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Loop through the running services
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }
    override fun onPause() {
        super.onPause()
        if (!isServiceRunning(serviceVibrator)) {
            startService(intentVibrator)
            vibratorButton.text = "Остановить вибрацию"
        } else {
            stopService(intentVibrator)
            vibratorButton.text = "Запустить вибрацию"
        }
    }
}