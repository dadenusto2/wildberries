package com.example.week21

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

// Вторая автивность с возможностью вызова AlertDialog через DialogFragment со всеми этапами ЖЦ
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

    fun callSecondActivity(view: View){
        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)
    }
}