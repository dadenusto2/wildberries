package com.example.week1.Receivers

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

// BroadcastReceiver для прослушивания режима самолета
class AirplaneReceiver : BroadcastReceiver()  {
    override fun onReceive(context: Context, intent: Intent) {// выполняется при обнаружении

        val builder = AlertDialog.Builder(context)//диалоговое окно

        val isAirplaneModeOn = intent.getBooleanExtra("state", false)// сосотояние режима самолета

        if (isAirplaneModeOn) {// если устройсво в режиме самолета
            builder.setTitle("Устройство в режиме самолета")
            builder.setMessage("Перейти в настройки для выключения режима в самолете?")
            // вызываем диалоговое окно, если в режиме самолета
            // Если выбираем да
            builder.setPositiveButton("Да"){dialogInterface, which ->
                // переходим в настройки режима в самолете
                val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
            // если нет, то остаемся
            builder.setNegativeButton("Нет"){dialogInterface, which ->
                Toast.makeText(context, "Устройство в режиме самолета", Toast.LENGTH_SHORT).show()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        } else {// если устройсво не в режиме самолета
            intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)
            Toast.makeText(context, "Устройство не в режиме самолета", Toast.LENGTH_SHORT).show()
        }
    }
}