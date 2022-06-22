package com.example.week62

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Основная активность, насследует интерфес для взаимодейсвия между фрагментами
 */
class MainActivity : AppCompatActivity(), OnFragmentSendDataListener {
    private lateinit var fragmentNumberGenerate: FragmentNumberGenerate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentNumberGenerate =
            supportFragmentManager.findFragmentById(R.id.fragment_pi) as FragmentNumberGenerate
    }

    /**
     * Изменеие статуса генерации
     *
     * @return новый статус
     */
    override fun changeGenerateStatus(): Boolean {
        return fragmentNumberGenerate.changeGenerateStatus()
    }

    /**
     * Сброс числа
     */
    override fun resetNumber() {
        fragmentNumberGenerate.resetNumber()
    }
}