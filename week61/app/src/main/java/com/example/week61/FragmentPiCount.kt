package com.example.week61

import android.annotation.SuppressLint
import android.content.Context
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.math.log
import kotlin.math.pow

const val STATUS_NONE = 0
const val STATUS_SET = 1
class FragmentPiCount: Fragment() {
    var b: Boolean = true
    var piSum :Int = 0x0
    var pi :Int = 0
    var k: Int = 1
    lateinit var thread: Thread
    lateinit var tv: TextView
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("---", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("---", "onCreate")
        arguments?.let {

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("---", "onCreateView")

        return inflater.inflate(R.layout.fragment_pi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv = view.findViewById(R.id.textView)
        val handlerThread = HandlerThread("MyBackgroundTask")
        handlerThread.start()
        val random = Random()

        piSign(2)
        val mHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                tv.append(msg.obj.toString())
                /*when(msg.what){
                    STATUS_SET ->{
                        //val newInt = random.nextInt(10)
                        tv.append(piSpigot(k))
                        k++
                    }
                }*/
            }
        }
        /*thread = Thread {
            //while(true) {
            for(i in 0..3)
                if (b) {
                    Thread.sleep(20)
                    val msg = Message.obtain()
                    //msg.obj = piSign(2)
                    piSign(2)
                    Log.d("---", piSign(i).toString())
                    mHandler.sendMessage(msg)
                    k++
                }
            //}
        }
        thread.start()*/
    }
    fun changeGenerateStatus(): Boolean{
        b=!b
        return b
    }
    fun resetNumber(){
        val curB = b
        if(curB)
            b=false
        tv.text =""
        b = curB
    }

    fun piSign(n:Int){
        var newPi: Int = 0x0
        for(i: Int in 0..4){
            val d = i.toDouble()
            Log.d("---",  (1/16.0.pow(d)*((120.0*d.pow(2)+157*i+47)/(52*d.pow(4)+1024*d.pow(3)+712*d.pow(2)+194*d+15))).toString())

        }
        Log.d("---", newPi.toString())
    }
}
