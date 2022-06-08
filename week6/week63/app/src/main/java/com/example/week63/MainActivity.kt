package com.example.week63

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), OnFragmentSendDataListener {
    lateinit var piFragment: FragmentPiCount
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        piFragment = supportFragmentManager.findFragmentById(R.id.fragment_pi) as FragmentPiCount
    }

    override fun changeGenerateStatus():Boolean{
        return piFragment.changeGenerateStatus()
    }

    override fun resetNumber(){
        piFragment.resetNumber()
    }
}