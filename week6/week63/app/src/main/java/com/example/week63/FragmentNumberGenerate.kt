package com.example.week63

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*

/**
 * Фрагмент генерации числа
 */
class FragmentNumberGenerate : Fragment() {
    var generateStatus: Boolean = false//запущена ли генерация?
    lateinit var tvNumber: TextView//число
    lateinit var tvNumberCount: TextView//кол-во чисел
    lateinit var job: Job

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
     * Запуск генерации
     */
    fun startGenerate() {
        job = lifecycleScope.launch {
            val numGenerate = startGenerateFlow()
            numGenerate.collect { newInt ->//получаем новую цифру
                tvNumber.append(newInt.toString())//добавляем к числу
                tvNumberCount.text = tvNumber.text.length.toString()//обновляем длину
            }
        }
    }

    /**
     * Обработка потка данных Flow
     */
    private suspend fun startGenerateFlow(): Flow<Int> = flow {
        val random = Random()
        while (generateStatus) {
            delay(10)//задержка
            val newInt = random.nextInt(10)//случайная цифра
            emit(newInt)//отправляем в поток
        }
    }

    /**
     * Изменеие статуса генерации
     *
     * @return новый статус
     */
    fun changeGenerateStatus(): Boolean {
        generateStatus = !generateStatus
        startGenerate()
        return generateStatus//возвращаем текущий стастус
    }

    /**
     * Сброс числа
     */
    fun resetNumber() {
        job.cancel()
        tvNumber.text = ""//сбрасываем числа и кол-во символов
        tvNumberCount.text = tvNumber.text.length.toString()
        //возвращаем исходный статус
        startGenerate()
    }

    override fun onPause() {
        generateStatus = false
        super.onPause()
    }
}
