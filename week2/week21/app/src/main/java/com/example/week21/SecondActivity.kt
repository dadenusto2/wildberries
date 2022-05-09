package com.example.week21

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

// Вторая автивность с возможностью вызова второй со всеми этапами ЖЦ
class SecondActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
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

    fun callAlertDialog(view: View){
        val myDialogFragment = DialogFragment()
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        myDialogFragment.show(transaction, "dialog")
    }
}