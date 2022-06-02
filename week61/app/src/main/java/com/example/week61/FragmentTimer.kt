package com.example.week61

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import java.util.*

class FragmentTimer : Fragment() {

    private var fragmentSendDataListener: OnFragmentSendDataListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("---", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("---", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("---", "onCreateView")
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val meter = view.findViewById<Chronometer>(R.id.c_meter)
        /*meter.setOnChronometerTickListener {
            val elapsedMillis: Long = (SystemClock.elapsedRealtime() - meter.base)
            Log.v("---+", elapsedMillis.toString())
        }*/
        val mHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                when(msg.what){
                    STATUS_SET ->{
                        val rnd = Random()
                        val red = rnd.nextInt(256)
                        val green = rnd.nextInt(256)
                        val blue = rnd.nextInt(256)
                        val color = Color.argb(255,red, green, blue)
                        if (1 - (0.299 * red + 0.587 * green + 0.114 * blue) / 255 < 0.5)
                            meter.setTextColor(Color.BLACK)
                        else
                            meter.setTextColor(Color.WHITE)
                        view.setBackgroundColor(color)
                    }
                }
            }
        }
        val thread = Thread {
            meter.base = SystemClock.elapsedRealtime();
            meter.start()
            while(true) {
                val elapsedMillis: Long = (SystemClock.elapsedRealtime() - meter.base)
                if(elapsedMillis.toDouble()%20000==0.0) {
                    mHandler.sendEmptyMessage(STATUS_SET)
                }
            }
        }
        thread.start()
        fragmentSendDataListener = context as OnFragmentSendDataListener
        val ibPlay = view.findViewById<ImageButton>(R.id.ib_play)
        ibPlay.setOnClickListener {
            val b = fragmentSendDataListener!!.changeGenerateStatus()
            if(b)
                ibPlay.setImageResource(android.R.drawable.ic_media_pause)
            else
                ibPlay.setImageResource(android.R.drawable.ic_media_play)

        }
        val ibReset = view.findViewById<ImageButton>(R.id.ib_reset)
        ibReset.setOnClickListener {
            fragmentSendDataListener!!.resetNumber();
        }
    }
}
interface OnFragmentSendDataListener {
    fun changeGenerateStatus():Boolean
    fun resetNumber()
}