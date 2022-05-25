package com.example.week3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button

//переход к ативностям
class MainActivity : AppCompatActivity() {
    lateinit var musicButtonConstr : Button
    lateinit var telegramButtonConstr  : Button
    lateinit var calculatorButtonConstr  : Button
    lateinit var facebookButtonConstr  : Button

    lateinit var musicButtonNoConstr : Button
    lateinit var telegramButtonNoConstr  : Button
    lateinit var calculatorButtonNoConstr  : Button
    lateinit var facebookButtonNoConstr  : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)

        //плеер
        musicButtonConstr  = findViewById<Button>(R.id.btn_music_costr)
        musicButtonConstr .setOnClickListener {
            val intent = Intent(
                this,
                MusicPlayerConstr::class.java
            )
            startActivity(intent)

        }
        //телерам
        telegramButtonConstr  = findViewById<Button>(R.id.btn_telegram_costr)
        telegramButtonConstr .setOnClickListener {
            val intent = Intent(
                this,
                TelegramActivityConstr::class.java
            )
            startActivity(intent)
        }
        //калькулятор
        calculatorButtonConstr  = findViewById<Button>(R.id.btn_calculator_costr)
        calculatorButtonConstr .setOnClickListener {
            val intent = Intent(
                this,
                CalculatorActivityConstr::class.java
            )
            startActivity(intent)
        }
        //фейсбук
        facebookButtonConstr  = findViewById<Button>(R.id.btn_facebook_constr)
        facebookButtonConstr .setOnClickListener {
            val intent = Intent(
                this,
                FaebookActivityConstr::class.java
            )
            startActivity(intent)
        }
        //плеер
        musicButtonNoConstr  = findViewById<Button>(R.id.btn_music_no_constr)
        musicButtonNoConstr .setOnClickListener {
            val intent = Intent(
                this,
                MusicPlayerConstr::class.java
            )
            startActivity(intent)

        }
        //telegram
        telegramButtonNoConstr  = findViewById<Button>(R.id.btn_telegram_no_constr)
        telegramButtonNoConstr .setOnClickListener {
            val intent = Intent(
                this,
                TelegramActivityNoCostr::class.java
            )
            startActivity(intent)
        }
        //калькулятор
        calculatorButtonNoConstr  = findViewById<Button>(R.id.btn_calculator_no_constr)
        calculatorButtonNoConstr .setOnClickListener {
            val intent = Intent(
                this,
                CalculatorActivityNoConstr::class.java
            )
            startActivity(intent)
        }
        //фесбук
        facebookButtonNoConstr  = findViewById<Button>(R.id.btn_facebook_no_constr)
        facebookButtonNoConstr .setOnClickListener {
            val intent = Intent(
                this,
                FaebookActivityNoConstr::class.java
            )
            startActivity(intent)
        }
    }
}