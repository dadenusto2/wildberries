package com.example.week1

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast


class AirplaneReceiver : BroadcastReceiver()  {
    override fun onReceive(context: Context, intent: Intent) {
        val builder = AlertDialog.Builder(context)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        val isAirplaneModeOn = intent.getBooleanExtra("state", false)
        if (isAirplaneModeOn) {
            builder.setTitle("Устройство в режиме самолета")
            builder.setMessage("Перейти в настройки для выключения режима в самолете?")
            builder.setPositiveButton("Да"){dialogInterface, which ->
                val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
            builder.setNegativeButton("Нет"){dialogInterface, which ->
                Toast.makeText(context, "Устройство в режиме самолета", Toast.LENGTH_SHORT).show()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        } else {
            intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)
            Toast.makeText(context, "Устройство не в режиме самолета", Toast.LENGTH_SHORT).show()
        }
    }
}