package com.example.week25

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

// активити, из которой вызывается первый фрагмент
// со всеми этапами ЖЦ
// Для чего нужен ЖЦ?

// ЖЦ нужен для правильной и надежной работы приложения, например для приложении с проигрыванием видео:
// если пользователь закрыл приложение, видео останавливается, если вернулся, то возобновляет видео,
// или правильную работы при получении вызова.
// Т.е. приложение будет знать, как вести себя в той или иной ситуации, что позволит избежать сбоев
// и обеспечит правильную работу приложения
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(this.toString(), "OnCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.i(this.toString(), "OnStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(this.toString(), "OnResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(this.toString(), "OnPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(this.toString(), "OnStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(this.toString(), "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(this.toString(), "OnDestroy")
    }
}