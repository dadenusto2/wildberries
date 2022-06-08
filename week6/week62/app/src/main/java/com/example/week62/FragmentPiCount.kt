package com.example.week62

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*
import kotlinx.coroutines.*
import androidx.lifecycle.*

class FragmentPiCount: Fragment() {
    var generateStatus: Boolean = false//запущена ли генерация?
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
        startGenerate()//запуск генерации
    }

    private fun startGenerate(){
        lifecycleScope.launch{
            val random = Random()
            coroutineScope {//для использования бесконечного цикла
                while (generateStatus) {
                    delay(10)//задержка
                    val newInt = random.nextInt(10)//случайная цифра
                    tvNumber.append(newInt.toString())//добавляем к числу
                    tvNumberCount.text = tvNumber.text.length.toString()//обновляем длину
                }
            }
        }
    }
    fun changeGenerateStatus(): Boolean{
        generateStatus=!generateStatus
        startGenerate()
        return generateStatus//возвращаем текущий стастус
    }
    fun resetNumber(){
        val curB = generateStatus//сохраняем текущий статус
        if(curB)//останавиваем, если запущен
            generateStatus=false
        tvNumber.text =""//сбрасываем числа и кол-во символов
        tvNumberCount.text = tvNumber.text.length.toString()
        //возвращаем исходный статус
        generateStatus = curB
        startGenerate()
    }
    override fun onPause() {
        generateStatus=false
        super.onPause()
    }
}
