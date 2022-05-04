package com.example.week1

import android.content.BroadcastReceiver
import android.content.ContentProvider
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.security.Provider

// Видимая часть приложения, графическое отображение интерфейса пользователя
// В данном приложении MainActivity расположены кнопки для пере хода к другим активностям android
// Activity применяется почти во всех приложения, написаных на Android
// Пример использования в приложениях: gmail, Telegram
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)// ресурс для разметки
    }
    //функции, срабатывающие по нажатию соотвествующих кнопок в activity_main
    fun BroadcastReceiver(view: View){
        val intent = Intent(this, ReceiverExampleActivity::class.java)// в намерении указваем класс, к котгорму хотим перейти
        startActivity(intent)// вызываем метод для запуска активности
    }
    fun ContentProvider(view: View){
        val intent = Intent(this, ContentExampleActivity::class.java)
        startActivity(intent)
    }
    fun Service(view: View){
        val intent = Intent(this, ServiceExampleActivity::class.java)
        startActivity(intent)
    }
}