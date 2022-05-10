package com.example.additionaltask1

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [ArrayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArrayFragment : Fragment() {
    class Coordinates     // getters and setters
        (val mas: Array<Int>, val time: Long)
    class FastTime     // getters and setters
        (val time: Long, val name: String, val thread: Boolean, var sort: ((array: Array<Int>) -> Coordinates), val nameId: Int) {
        override fun toString(): String {
            return "FastTime(time=$time, name='$name', thread=$thread, sort=$sort, nameId=$nameId)"
        }
    }

    // TODO: Rename and change types of parameters
    private var countOfElement: Int = 0
    private lateinit var array : Array<Int>
    private lateinit var listSort: ListView
    private lateinit var algorithmName: TextView
    private lateinit var algorithmTime: TextView
    private lateinit var btnBubble: Button
    private lateinit var btnSelection : Button
    private lateinit var btnInsertion: Button
    private lateinit var btnQuick: Button
    private lateinit var btnStandart: Button
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countOfElement = it.getInt(ARG_PARAM1)
        }
        array = Array(countOfElement) { 0 }
        for (i in 0..countOfElement-1){
            array[i] = (0..countOfElement).random()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_array, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listView = view.findViewById<ListView>(R.id.original_list)
        listView.adapter =
            activity?.let {
                ArrayAdapter(it.applicationContext, R.layout.list_item, R.id.text1, array)
            }

        listSort = view.findViewById(R.id.sorted_list)

        val spinner: Spinner = view.findViewById(R.id.spinner_sort)
        val sort = arrayOf(
                            "Выберите метод сортировки",
                            resources.getString(R.string.bubble_sort_name),
                            resources.getString(R.string.selection_sort_name),
                            resources.getString(R.string.insertion_sort_name),
                            resources.getString(R.string.quick_sort_name),
                            resources.getString(R.string.standart_sort_name))
        spinner.adapter =
            activity?.let {
                ArrayAdapter(it.applicationContext, R.layout.spinner_item, R.id.textView3, sort)
            }

        val checkBox = requireView().findViewById<CheckBox>(R.id.check_box_thread)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    // Получаем выбранный объект
                    val item = parent.getItemAtPosition(position) as String
                    when (item) {
                        sort[1] -> onClickFun(::myBubbleSort, checkBox.isChecked, true, R.string.bubble_sort_name)
                        sort[2] -> onClickFun(::selectionSort, checkBox.isChecked, true, R.string.selection_sort_name)
                        sort[3] -> onClickFun(::insertionSort, checkBox.isChecked, true, R.string.insertion_sort_name)
                        sort[4] -> onClickFun(::quickSort, checkBox.isChecked, true, R.string.quick_sort_name)
                        sort[5] -> onClickFun(::standartSort, checkBox.isChecked, true, R.string.standart_sort_name)
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        spinner.onItemSelectedListener = itemSelectedListener

        algorithmName = view.findViewById(R.id.algorithm_name)
        algorithmTime = view.findViewById(R.id.algorithm_time)

        /*btnBubble = view.findViewById(R.id.bubble_sort)
        btnSelection = view.findViewById(R.id.selection_sort)
        btnInsertion = view.findViewById(R.id.insertion_sort)
        btnQuick = view.findViewById(R.id.quick_sort)
        btnStandart = view.findViewById(R.id.standart_sort)*/

        val btnFast: Button = view.findViewById(R.id.faster_algorithm)
        btnFast.setOnClickListener {
            handler = Handler()
            val thread = Thread(Runnable {
                fasterAlgorithm()
            })
            thread.start()
            try {
                thread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        val btnShiffle: Button = view.findViewById(R.id.shuffle)
        btnShiffle.setOnClickListener {
            shuffle()
            listView.adapter =
                activity?.let {
                    ArrayAdapter(it.applicationContext, R.layout.list_item, R.id.text1, array)
                }
        }


        /*btnBubble.setOnClickListener {
            onClickFun(::myBubbleSort, checkBox.isChecked, true, R.string.bubble_sort_name)
        }

        btnSelection .setOnClickListener {
            onClickFun(::selectionSort, checkBox.isChecked, true, R.string.selection_sort_name)
        }

        btnInsertion.setOnClickListener {
            onClickFun(::insertionSort, checkBox.isChecked, true, R.string.insertion_sort_name)
        }

        btnQuick.setOnClickListener {
            onClickFun(::quickSort,checkBox.isChecked, true, R.string.quick_sort_name)
        }

        btnStandart.setOnClickListener {
            onClickFun(::standartSort,checkBox.isChecked, true, R.string.standart_sort_name)
        }*/
    }

    fun chengeView(workTime: Long, name: String, mas: Array<Int>){
        algorithmTime.text = workTime.toString()
        algorithmName.text = name
        listSort.adapter =
            activity?.let {
                ArrayAdapter(it.applicationContext, R.layout.list_item, R.id.text1, mas)
            }
        //Toast.makeText(getContext(), "Завершена работа через Thread", Toast.LENGTH_SHORT).show()
        //btnEnabled(true)
    }

    /*fun btnEnabled(b: Boolean = true){
        btnBubble.isEnabled  = b
        btnSelection.isEnabled  = b
        btnInsertion.isEnabled  = b
        btnQuick.isEnabled  = b
    }*/

    fun fasterAlgorithm(){
        val time = arrayOf(
                FastTime(
                    onClickFun(::myBubbleSort, true, false, R.string.bubble_sort_name),
                    "myBubbleSort",
                    true,
                    ::myBubbleSort,
                    R.string.bubble_sort_name
                ),
                FastTime(
                    onClickFun(::selectionSort, true, false, R.string.selection_sort_name),
                    "selectionSort",
                    true,
                    ::selectionSort,
                    R.string.selection_sort_name
                ),
                FastTime(
                    onClickFun(::insertionSort, true, false, R.string.insertion_sort_name),
                    "insertionSort",
                    true,
                    ::insertionSort,
                    R.string.insertion_sort_name
                ),
                FastTime(
                    onClickFun(::quickSort, true, false, R.string.quick_sort_name),
                    "quickSort",
                    true,
                    ::quickSort,
                    R.string.quick_sort_name
                ),

                FastTime(
                    onClickFun(::myBubbleSort, false, false, R.string.bubble_sort_name),
                    "myBubbleSort",
                    false,
                    ::myBubbleSort,
                    R.string.bubble_sort_name
                ),
                FastTime(
                    onClickFun(::selectionSort, false, false, R.string.selection_sort_name),
                    "selectionSort",
                    false,
                    ::selectionSort,
                    R.string.selection_sort_name
                ),
                FastTime(
                    onClickFun(::insertionSort, false, false, R.string.insertion_sort_name),
                    "insertionSort",
                    false,
                    ::insertionSort,
                    R.string.insertion_sort_name
                ),
                FastTime(
                    onClickFun(::quickSort, false, false, R.string.quick_sort_name),
                    "quickSort",
                    false,
                    ::quickSort,
                    R.string.quick_sort_name
                )
            )

        var minTime: Long = time[0].time
        var minIndex = 0

        for(i in 0 until time.size){
            Log.d("Time", time[i].toString())
        }
        for(i in 1 until time.size){
            if(time[i].time < minTime) {
                minTime = time[i].time
                minIndex = i
            }
        }

        Log.d("Time", minIndex.toString())
        val runnable = Runnable {
            Toast.makeText(
                getContext(), "Самое маленькое время: " + minTime
                        + " у алгоритма: " + time[minIndex].name + ", Thread: " + time[minIndex].thread,
                Toast.LENGTH_LONG
            ).show()
        }
        handler.postDelayed(runnable, 0)
        //btnEnabled(true)
    }

    fun onClickFun(sort: (array: Array<Int>) -> Coordinates, isChecked:Boolean, isCreateView: Boolean, nameId: Int) : Long{
            //btnEnabled(false)
        var time: Long = Long.MAX_VALUE
        if(isChecked) {
            //Toast.makeText(getContext(), "Работает через Thread", Toast.LENGTH_SHORT).show()
            val thread = Thread(Runnable {
                val mas = array.clone()
                val coordinates: Coordinates = sort(mas)
                time = coordinates.time
                if(isCreateView) {
                    val runnable = Runnable {
                        chengeView(
                            coordinates.time,
                            resources.getString(nameId),
                            coordinates.mas
                        )
                    }
                    handler = Handler()
                    handler.postDelayed(runnable, 0)
                }
            })
            thread.start()
            try {
                thread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            //Log.i("end","Поток отработан" + time)
            return time
        }
        else{
            val mas = array.clone()
            val coordinates: Coordinates = sort(mas)
            if(isCreateView) {
                chengeView(
                    coordinates.time,
                    resources.getString(nameId),
                    coordinates.mas
                )
            }
            time = coordinates.time
            return time
        }
    }

    fun shuffle(){
        array.shuffle()
    }

    fun myBubbleSort(array: Array<Int>): Coordinates {
        var isSorted = false
        var buf: Int
        val time = System.currentTimeMillis()
        while (!isSorted) {
            isSorted = true
            for (i in 0..array.size - 2) {
                if (array[i] > array[i + 1]) {
                    isSorted = false
                    buf = array[i]
                    array[i] = array[i + 1]
                    array[i + 1] = buf
                }
            }
        }
        val workTime: Long = System.currentTimeMillis() - time
        return Coordinates(array, workTime)
    }


    fun insertionSort(array: Array<Int>): Coordinates {
        val mas = array.clone()
        val time = System.currentTimeMillis()
        for (i in 1 until array.size) {
            val current = mas[i]
            var j = i - 1
            while (j >= 0 && current < mas[j]) {
                mas[j + 1] = mas[j]
                j--
            }
            // в этой точке мы вышли, так что j так же -1
            // или в первом элементе, где текущий >= a[j]
            mas[j + 1] = current
        }
        val workTime: Long = System.currentTimeMillis() - time
        return Coordinates(mas, workTime)
    }

    fun selectionSort(array: Array<Int>): Coordinates {
        val time = System.currentTimeMillis()
        for (i in 0 until array.size - 1) {
            var minPos = i
            for (j in i + 1 until array.size) {
                if (array[j].compareTo(array[minPos]) < 0) {
                    minPos = j
                }
            }
            val saveValue = array[minPos]
            array[minPos] = array[i]
            array[i] = saveValue
        }

        val workTime: Long = System.currentTimeMillis() - time
        return Coordinates(array, workTime)
    }

    fun quickSort(array: Array<Int>): Coordinates {
        val startIndex = 0
        val endIndex: Int = array.size - 1
        val time = System.currentTimeMillis()
        doSort(array, startIndex, endIndex)
        val workTime: Long = System.currentTimeMillis() - time
        return Coordinates(array, workTime)
    }
    
    fun standartSort(array: Array<Int>): Coordinates {
        val time = System.currentTimeMillis()
        val mas = array.sortedArray()
        val workTime: Long = System.currentTimeMillis() - time
        return Coordinates(mas, workTime)
    }

    private fun doSort(array: Array<Int>, start: Int, end: Int) {
        if (start >= end) return
        var i = start
        var j = end
        var cur = i - (i - j) / 2
        while (i < j) {
            while (i < cur && array[i] <= array[cur]) {
                i++
            }
            while (j > cur && array[cur] <= array[j]) {
                j--
            }
            if (i < j) {
                val temp = array[i]
                array[i] = array[j]
                array[j] = temp
                if (i == cur) cur = j else if (j == cur) cur = i
            }
        }
        doSort(array, start, cur)
        doSort(array,cur + 1, end)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(countOfElement: Int) =
            ArrayFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, countOfElement)
                }
            }
    }
}