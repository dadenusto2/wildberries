package com.example.week62

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), OnFragmentSendDataListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //запуск/останоква генерации
    override fun changeGenerateStatus():Boolean{
        val piFragment : FragmentPiCount = supportFragmentManager.findFragmentById(R.id.fragment_pi)
                as FragmentPiCount
        return piFragment.changeGenerateStatus()
    }
    //сброс сгенерированого числа
    override fun resetNumber(){
        val piFragment : FragmentPiCount = supportFragmentManager.findFragmentById(R.id.fragment_pi)
                as FragmentPiCount
        piFragment.resetNumber()
    }
}