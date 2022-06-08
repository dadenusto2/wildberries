package com.example.week63

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

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
        startGenerate()
    }
    //функция для генерации
    fun startGenerate(){
        lifecycleScope.launch {
            val numGenerate = startGenerateFlow()
            numGenerate.collect{ newInt ->//получаем новую цифру
                tvNumber.append(newInt.toString())//добавляем к числу
                tvNumberCount.text = tvNumber.text.length.toString()//обновляем длину
            }
        }
    }
    //функция flow, обрабатывающая потоки данных
    private suspend fun startGenerateFlow(): Flow<Int> = flow {
        val random = Random()
        while(generateStatus) {
            delay(10)//задержка
            val newInt = random.nextInt(10)//случайная цифра
            emit(newInt)//отправляем в поток
        }
    }
    //изменениея статуса генерации
    fun changeGenerateStatus(): Boolean{
        generateStatus=!generateStatus
        startGenerate()
        return generateStatus
    }
    //сброс числа
    fun resetNumber(){
        val curB = generateStatus//сохраняем текущий статус
        if(curB)//останавиваем, если запущен
            generateStatus=false
        //сбрасываем число и кол-во символов
        tvNumber.text =""
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
