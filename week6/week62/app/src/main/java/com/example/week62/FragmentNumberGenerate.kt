package com.example.week62

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
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
        startGenerate()//запуск генерации
    }

    /**
     * Запуск генерации
     */
    private fun startGenerate() {
        job = lifecycleScope.launch {
            val random = Random()
            coroutineScope {//для использования бесконечного цикла
                while (generateStatus) {
                    delay(5)//задержка
                    val newInt = random.nextInt(10)//случайная цифра
                    tvNumber.append(newInt.toString())//добавляем к числу
                    tvNumberCount.text = tvNumber.text.length.toString()//обновляем длину
                }
            }
            Log.d("---", "stop")
        }
        job.start()
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
