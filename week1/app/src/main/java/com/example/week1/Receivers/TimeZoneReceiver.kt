package com.example.week1.Receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.widget.Toast
import com.example.week1.R
import java.util.*

// данный receiver работает во всей системе и зарегестрирован в манифесте
// для активации измените часовой пояс
class TimeZoneReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {// выполняется при обнаружении
        if (intent.action == Intent.ACTION_TIMEZONE_CHANGED) {// если изменен часовой пояс
            Toast.makeText(context, Resources.getSystem().getString(R.string.app_name) +
                    ": Изменен часовой пояс на " +
                    TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT), Toast.LENGTH_LONG)
                .show()
        }
    }
}