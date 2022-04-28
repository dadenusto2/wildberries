package com.example.week1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var btn_Receiver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_Receiver = findViewById(R.id.btn_Receiver)
        btn_Receiver.setOnClickListener(listener)
    }
    val listener= View.OnClickListener { view ->
        when (view.getId()) {
            R.id.btn_Receiver -> {
                val intent = Intent(this, ReceiverExampleActivity::class.java)
                startActivity(intent)
            }
        }
    }
}