package com.example.additionaltask1

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"

class ArrayListFragment : Fragment() {
    /**
     * Отсортированный массив и время сортировки
     *
     * @property mas - отсортированный массив
     * @property time - время сортировки
     * @constructor Create empty Array and time
     */
    class ArrayAndTime     // для хранения структуры и времени работы
        (val mas: ArrayList<Int>, val time: Long)

    /**
     * Для хранения сортировки, ее имени, через потоки, id названия
     *
     * @property time - время сортировки
     * @property thread - через потоки или нет
     * @property nameId - ссылка на название алгоритма
     * @constructor Create empty Fast time
     */
    class FastTime     // getters and setters
        (val time: Long, val thread: Boolean, val nameId: Int)

    private var countOfElement: Int = 0
    private var array : ArrayList<Int> = ArrayList()
    private lateinit var listSort: ListView
    private lateinit var algorithmName: TextView
    private lateinit var algorithmTime: TextView
    private lateinit var algorithmThread: TextView
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countOfElement = it.getInt(ARG_PARAM1)
        }
        for (i in 0..countOfElement-1){
            array.add((0..countOfElement).random())
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
        Log.d("Type of array", array::class.java.typeName)
        val listView = view.findViewById<ListView>(R.id.original_list)
        listView.adapter =
            activity?.let {
                ArrayAdapter(it.applicationContext, R.layout.list_item, R.id.text1, array)
            }

        view.findViewById<TextView>(R.id.tv_array_size).text = array.size.toString()

        listSort = view.findViewById(R.id.sorted_list)

        val spinner: Spinner = view.findViewById(R.id.spn_algorithm)
        val sort = arrayOf(
                            "Выберите метод сортировки",
                            resources.getString(R.string.bubble_sort_name),
                            resources.getString(R.string.selection_sort_name),
                            resources.getString(R.string.insertion_sort_name),
                            resources.getString(R.string.quick_sort_name),
                            resources.getString(R.string.standart_sort_name))
        spinner.adapter =
            activity?.let {
                ArrayAdapter(it.applicationContext, R.layout.spinner_item, R.id.tv_spinner, sort)
            }

        val checkBox = requireView().findViewById<CheckBox>(R.id.check_box_thread)// через thread
        checkBox.setOnClickListener(){
            spinner.setSelection(0)
        }

        algorithmName = view.findViewById(R.id.algorithm_name)// название алгоритма
        algorithmTime = view.findViewById(R.id.algorithm_time)// время работы алгоритма
        algorithmThread = view.findViewById(R.id.algorithm_thread)// время работы алгоритма

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
                        sort[1] -> onClickFun(::bubbleSort, checkBox.isChecked, true, R.string.bubble_sort_name)
                        sort[2] -> onClickFun(::selectionSort, checkBox.isChecked, true, R.string.selection_sort_name)
                        sort[3] -> onClickFun(::insertionSort, checkBox.isChecked, true, R.string.insertion_sort_name)
                        sort[4] -> onClickFun(::quickSort, checkBox.isChecked, true, R.string.quick_sort_name)
                        sort[5] -> onClickFun(::standartSort, checkBox.isChecked, true, R.string.standart_sort_name)
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        spinner.onItemSelectedListener = itemSelectedListener

        val btnFast: Button = view.findViewById(R.id.btn_fasterAlgorithm)// для поиска самого быстрого алгоритма
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
        
        val btnShiffle: Button = view.findViewById(R.id.btn_shuffleArray)// для перемешивания массива
        btnShiffle.setOnClickListener {
            array.shuffle()
            listView.adapter =
                activity?.let {
                    ArrayAdapter(it.applicationContext, R.layout.list_item, R.id.text1, array)
                }
        }
    }
    
    /**
     * Отображение отсортированного массива
     *
     * @param workTime - время работы
     * @param name - название алгортма
     * @param mas-отсортированный массив
     */
    private fun chengeView(workTime: Long, name: String, mas: ArrayList<Int>, isThread: Boolean){
        algorithmTime.text = workTime.toString()
        algorithmName.text = name
        algorithmThread.text = isThread.toString()
        listSort.adapter =
            activity?.let {
                ArrayAdapter(it.applicationContext, R.layout.list_item, R.id.text1, mas)
            }
    }

    /**
     * Поиск самого быстрого алгоритма путем перебора всех возможных алгоритмов
     */
    private fun fasterAlgorithm(){
        val time = arrayOf(
                FastTime(
                    onClickFun(::bubbleSort, true, false, R.string.bubble_sort_name),
                    true,
                    R.string.bubble_sort_name
                ),
                FastTime(
                    onClickFun(::selectionSort, true, false, R.string.selection_sort_name),
                    true,
                    R.string.selection_sort_name
                ),
                FastTime(
                    onClickFun(::insertionSort, true, false, R.string.insertion_sort_name),
                    true,
                    R.string.insertion_sort_name
                ),
                FastTime(
                    onClickFun(::quickSort, true, false, R.string.quick_sort_name),
                    true,
                    R.string.quick_sort_name
                ),
                FastTime(
                    onClickFun(::standartSort, true, false, R.string.standart_sort_name),
                    true,
                    R.string.standart_sort_name
                ),
                FastTime(
                    onClickFun(::bubbleSort, false, false, R.string.bubble_sort_name),
                    false,
                    R.string.bubble_sort_name
                ),
                FastTime(
                    onClickFun(::selectionSort, false, false, R.string.selection_sort_name),
                    false,
                    R.string.selection_sort_name
                ),
                FastTime(
                    onClickFun(::insertionSort, false, false, R.string.insertion_sort_name),
                    false,
                    R.string.insertion_sort_name
                ),
                FastTime(
                    onClickFun(::quickSort, false, false, R.string.quick_sort_name),
                    false,
                    R.string.quick_sort_name
                ),
                FastTime(
                    onClickFun(::standartSort, false, false, R.string.standart_sort_name),
                    true,
                    R.string.standart_sort_name
                )
            )

        // находим наименьшее время
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
                        + " у алгоритма: " + resources.getString(time[minIndex].nameId) + ", Thread: " + time[minIndex].thread,
                Toast.LENGTH_LONG
            ).show()
        }
        handler.postDelayed(runnable, 0)
        //btnEnabled(true)
    }

    /**
     * Сортировка выбранным алгоритмом
     *
     * @param sort функция сортировки
     * @param isThread через потоки или нет
     * @param isCreateView отображать ли отсортированный список
     * @param nameId id строки с названием алгоритма
     * @receiver
     * @return время работы алгоритма в мс
     */
    fun onClickFun(sort: (array: ArrayList<Int>) -> ArrayAndTime, isThread:Boolean, isCreateView: Boolean, nameId: Int) : Long{
        var time: Long = Long.MAX_VALUE // Для времени работы
        if(isThread) {
            if(isCreateView)// если создаем отбражение, то handler для отображения
                handler = Handler()
            val thread = Thread(Runnable {// запускам сортировку
                val mas = array.clone() as ArrayList<Int>// клонируем массив для сортировки
                val ArrayAndTime: ArrayAndTime = sort(mas)// выполняем сортировку указзанной функцией
                time = ArrayAndTime.time// время работы
                if(isCreateView) {// если отображаем список
                    val runnable = Runnable {
                        chengeView(
                            ArrayAndTime.time,
                            resources.getString(nameId),
                            ArrayAndTime.mas,
                            isThread
                        )
                    }
                    handler.postDelayed(runnable, 0)
                }
            })
            thread.start()
            if(!isCreateView) {// если не отображаем, ожидаем завершения потока для получения времени работы
                try {
                    thread.join()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            return time
        }
        else{// если не через поток
            val mas = array.clone()
            val ArrayAndTime: ArrayAndTime = sort(mas as ArrayList<Int>)
            if(isCreateView) {
                chengeView(
                    ArrayAndTime.time,
                    resources.getString(nameId),
                    ArrayAndTime.mas,
                    isThread
                )
            }
            time = ArrayAndTime.time
            return time
        }
    }

    /**
     * Сортировка пузырьком
     *
     * @param array список для сортировки
     * @return класс, содержащий исходный массив и время работы
     */
    fun bubbleSort(array: ArrayList<Int>): ArrayAndTime {
        var isSorted = false
        var buf: Int
        val time = System.currentTimeMillis()
        while (!isSorted) {
            isSorted = true
            for (i in 0..array.size - 2) {
                if (array[i] > array[i + 1]) {// если следуюший меньше предыдущего меняем
                    isSorted = false// значит массив не отсортирован
                    Collections.swap(array, i, i + 1)
                }
            }
        }
        val workTime: Long = System.currentTimeMillis() - time
        return ArrayAndTime(array, workTime)
    }

    /**
     * Сортировка вставками
     * *
     * @param array список для сортировки
     * @return класс, содержащий исходный массив и время работы
     */
    fun insertionSort(array: ArrayList<Int>): ArrayAndTime {
        val time = System.currentTimeMillis()
        for (i in 1 until array.size) {
            val current = array[i]// берем текущий элемент
            var j = i - 1
            //просмативаем предыдущие и если текущий меньше предыдущего, меняем их местами
            while (j >= 0 && current < array[j]) {
                array[j + 1] = array[j]
                j--
            }
            array[j + 1] = current
        }
        val workTime: Long = System.currentTimeMillis() - time
        return ArrayAndTime(array, workTime)
    }

    /**
     * Сортировка выбором
     *
     * @param array список для сортировки
     * @return класс, содержащий исходный массив и время работы
     */
    fun selectionSort(array: ArrayList<Int>): ArrayAndTime {
        val time = System.currentTimeMillis()
        for (i in 0 until array.size - 1) {
            var minPos = i
            for (j in i + 1 until array.size) {
                if (array[j] < array[minPos]) {// находим минимальный элемент с текущего индекса
                    minPos = j
                }
            }
            //меняем местами минимальный и текущий, если разные
            if(minPos != i)
                Collections.swap(array, i, minPos)
        }

        val workTime: Long = System.currentTimeMillis() - time
        return ArrayAndTime(array, workTime)
    }

    /**
     * Быстрая сортировка
     *
     * @param array список для сортировки
     * @return класс, содержащий исходный массив и время работы
     */
    fun quickSort(array: ArrayList<Int>): ArrayAndTime {
        val startIndex = 0
        val endIndex: Int = array.size - 1
        val time = System.currentTimeMillis()
        doQuickSort(array, startIndex, endIndex)// запускаем рекурсию
        val workTime: Long = System.currentTimeMillis() - time
        return ArrayAndTime(array, workTime)
    }

    private fun doQuickSort(array: ArrayList<Int>, start: Int, end: Int) {
        if (start >= end) return
        var i = start
        var j = end
        // разбиваем массив опорным элементом
        var cur = i - (i - j) / 2
        while (i < j) {
            // доходим до элемента слева, большого чем опорный элемент
            while (i < cur && array[i] <= array[cur]) {
                i++
            }
            // доходим до элемента справа, меньшего чем опорный элемент
            while (j > cur && array[cur] <= array[j]) {
                j--
            }
            if (i < j) {// меняем местами эти элементы
                Collections.swap(array, i, j)
                if (i == cur) cur = j else if (j == cur) cur = i
            }
        }
        // сортируем справа и слево тот опроного
        doQuickSort(array, start, cur)
        doQuickSort(array,cur + 1, end)
    }

    /**
     * Стандартный алгоритм сортировки для структуры
     *
     * @param array список для сортировки
     * @return класс, содержащий исходный массив и время работы
     */
    fun standartSort(array: ArrayList<Int>): ArrayAndTime {
        val time = System.currentTimeMillis()
        array.sort()
        val workTime: Long = System.currentTimeMillis() - time
        return ArrayAndTime(array, workTime)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(countOfElement: Int) =
            ArrayListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, countOfElement)
                }
            }
    }
}