package com.example.week61

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*

/**
 * Фрагмент генерации числа
 */
class FragmentNumberGenerate : Fragment() {
    var generateStatus: Boolean = false//запущена ли генерация?
    lateinit var thread: Thread
    lateinit var tvNumber: TextView//число
    lateinit var tvNumberCount: TextView//кол-во чисел
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvNumber = view.findViewById(R.id.tv_number)
        tvNumberCount = view.findViewById(R.id.tv_number_count)
        tvNumberCount.text = tvNumber.text.length.toString()
        startGenerate()
    }

    /**
     * Функция генерации
     */
    private fun startGenerate() {
        val mHandler = Handler(Looper.getMainLooper())
        val random = Random()
        thread = Thread {
            while (generateStatus) {//бесконечный цикл
                Thread.sleep(50)//пауза
                val newInt = random.nextInt(10)//случайная цифра
                mHandler.post {
                    tvNumber.append(newInt.toString())//добавляем к числу
                    tvNumberCount.text = tvNumber.text.length.toString()//обновляем длину
                }
            }
        }
        thread.start()
    }

    /**
     * Изменеие статуса генерации
     *
     * @return новый статус
     */
    fun changeGenerateStatus(): Boolean {
        generateStatus = !generateStatus
        startGenerate()
        return generateStatus
    }

    /**
     * Сброс числа
     */
    fun resetNumber() {
        //сбрасываем число и кол-во символов
        thread = Thread()
        tvNumber.text = ""
        tvNumberCount.text = tvNumber.text.length.toString()
        startGenerate()
    }

    override fun onPause() {
        generateStatus = false
        super.onPause()
    }
}
