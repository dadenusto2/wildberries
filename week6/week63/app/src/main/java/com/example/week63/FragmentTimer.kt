package com.example.week63

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*

/**
 * фрагмент таймера, остановки/запуска и сброса
 */
class FragmentTimer : Fragment() {
    private var fragmentSendDataListener: OnFragmentSendDataListener? = null
    lateinit var chronometer: Chronometer//для вывода времени рабрты
    lateinit var ibPlay: ImageButton//кнопка паузы
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chronometer = view.findViewById(R.id.c_meter)
        chronometer.start()//запускаем время
        chronometer.setOnChronometerTickListener {//прослушиваем каждое изменение
            // время в милисекундах
            val elapsedMillis: Long = (SystemClock.elapsedRealtime() - chronometer.base)
            //если делится на 20 и не нулевая секунда
            if ((elapsedMillis / 1000 % 20).toDouble() == 0.0 && elapsedMillis > 1000) {
                lifecycleScope.launch {
                    changeColor().collect { color ->//получаем пару [светлое ли?, фона]
                        if (color.first)//если светлый, текст времени черный
                            chronometer.setTextColor(Color.BLACK)
                        else//если темный, текст времени белый
                            chronometer.setTextColor(Color.WHITE)
                        view.setBackgroundColor(color.second)//меняем цвет фона
                    }
                }
            }
        }
        //берем из контекста объект, наследующий интерфейс OnFragmentSendDataListener
        fragmentSendDataListener = context as OnFragmentSendDataListener
        //кнопка запуска/остановки
        ibPlay = view.findViewById(R.id.ib_play)
        ibPlay.setOnClickListener {
            // меням состояние и возвращаем текущее
            val b = fragmentSendDataListener!!.changeGenerateStatus()
            if (b)
                ibPlay.setImageResource(android.R.drawable.ic_media_pause)
            else
                ibPlay.setImageResource(android.R.drawable.ic_media_play)
        }
        //сброс числа
        val ibReset = view.findViewById<ImageButton>(R.id.ib_reset)
        ibReset.setOnClickListener {
            fragmentSendDataListener!!.resetNumber()
        }
    }

    /**
     * Изменение цвета фона
     */
    private suspend fun changeColor(): Flow<Pair<Boolean, Int>> = flow {
        val rnd = Random()
        // генерируем случайный цвет для фона
        val red = rnd.nextInt(256)
        val green = rnd.nextInt(256)
        val blue = rnd.nextInt(256)
        val color = Color.argb(255, red, green, blue)
        //какой цвет: темный или светлый
        val lightness = 1 - (0.299 * red + 0.587 * green + 0.114 * blue) / 255 < 0.5
        emit(Pair(lightness, color))//отправляем в поток
    }

    override fun onPause() {
        ibPlay.setImageResource(android.R.drawable.ic_media_play)
        super.onPause()
    }
}