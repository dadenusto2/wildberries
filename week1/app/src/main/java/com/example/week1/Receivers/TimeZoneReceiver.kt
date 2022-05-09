package com.example.week1.Receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


// данный receiver работает во всей системе и зарегестрирован в манифесте
// для активации измените часовой пояс
class TimeZoneReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {// выполняется при обнаружении
        if (intent.action == Intent.ACTION_TIMEZONE_CHANGED) {// если изменен часовой пояс
            Log.d("TimeZone", intent.action.toString())
        }
    }
}