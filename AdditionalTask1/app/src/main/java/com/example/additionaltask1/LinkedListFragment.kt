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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [LinkedListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LinkedListFragment : Fragment() {
    /**
     * Отсортированный массив и время сортировки
     *
     * @property mas - отсортированный массив
     * @property time - время сортировки
     * @constructor Create empty Array and time
     */
    class arrayAndTime     // для хранения структуры и времени работы
        (val mas: LinkedList<Int>, val time: Long)

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

    // TODO: Rename and change types of parameters
    private var countOfElement: Int = 0
    private var array : LinkedList<Int> = LinkedList()
    private lateinit var listSort: ListView
    private lateinit var algorithmName: TextView
    private lateinit var algorithmTime: TextView
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
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
        val listView = view.findViewById<ListView>(R.id.original_list)
        listView.adapter =
            activity?.let {
                ArrayAdapter(it.applicationContext, R.layout.list_item, R.id.text1, array)
            }

        view.findViewById<TextView>(R.id.tv_array_size).text = array.size.toString()

        listSort = view.findViewById(R.id.sorted_list) // для отсортированного массива

        val spinner: Spinner = view.findViewById(R.id.spn_algorithm)// для выбора алгоритма
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

        val checkBox = requireView().findViewById<CheckBox>(R.id.check_box_thread)// через thread

        algorithmName = view.findViewById(R.id.algorithm_name)// название алгоритма
        algorithmTime = view.findViewById(R.id.algorithm_time)// время работы алгоритма

        // выбор алгоритма сортироки
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
                        sort[0] -> {// если ничего, то пустой отсортированный список
                            listSort.adapter =
                                activity?.let {
                                    ArrayAdapter(it.applicationContext, R.layout.list_item, R.id.text1, arrayOfNulls<Int>(0))
                                }
                            algorithmName.text = ""
                            algorithmTime.text = ""
                        }
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

        val btnFast: Button = view.findViewById(R.id.faster_algorithm)// для поиска самогобыстрого алгоритма
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

        val btnShiffle: Button = view.findViewById(R.id.shuffle)// для перемешивания массива
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
    fun chengeView(workTime: Long, name: String, mas: LinkedList<Int>){
        algorithmTime.text = workTime.toString()
        algorithmName.text = name
        listSort.adapter =
            activity?.let {
                ArrayAdapter(it.applicationContext, R.layout.list_item, R.id.text1, mas)
            }
    }

    /**
     * Ищет саммый быстрый алгоритм путем перебора всех возможных алгоритмов
     */
    fun fasterAlgorithm(){
        // для хранения времени работы, через потоки ли алгоритм и название алгоритма 
        val time = arrayOf(
            LinkedListFragment.FastTime(
                onClickFun(::bubbleSort, true, false, R.string.bubble_sort_name),
                true,
                R.string.bubble_sort_name
            ),
            LinkedListFragment.FastTime(
                onClickFun(::selectionSort, true, false, R.string.selection_sort_name),
                true,
                R.string.selection_sort_name
            ),
            LinkedListFragment.FastTime(
                onClickFun(::insertionSort, true, false, R.string.insertion_sort_name),
                true,
                R.string.insertion_sort_name
            ),
            LinkedListFragment.FastTime(
                onClickFun(::quickSort, true, false, R.string.quick_sort_name),
                true,
                R.string.quick_sort_name
            ),
            LinkedListFragment.FastTime(
                onClickFun(::standartSort, true, false, R.string.standart_sort_name),
                true,
                R.string.standart_sort_name
            ),
            LinkedListFragment.FastTime(
                onClickFun(::bubbleSort, false, false, R.string.bubble_sort_name),
                false,
                R.string.bubble_sort_name
            ),
            LinkedListFragment.FastTime(
                onClickFun(::selectionSort, false, false, R.string.selection_sort_name),
                false,
                R.string.selection_sort_name
            ),
            LinkedListFragment.FastTime(
                onClickFun(::insertionSort, false, false, R.string.insertion_sort_name),
                false,
                R.string.insertion_sort_name
            ),
            LinkedListFragment.FastTime(
                onClickFun(::quickSort, false, false, R.string.quick_sort_name),
                false,
                R.string.quick_sort_name
            ),
            LinkedListFragment.FastTime(
                onClickFun(::standartSort, false, false, R.string.standart_sort_name),
                true,
                R.string.standart_sort_name
            )
        )

        // находим наименьшее время
        var minTime: Long = time[0].time
        var minIndex = 0

        for(i in 1 until time.size){
            if(time[i].time < minTime) {
                minTime = time[i].time
                minIndex = i
            }
        }
        // Вывод наибыстрейшего алгоритма, времени работы и через потоки ли
        Log.d("Time", minIndex.toString())
        val runnable = Runnable {
            Toast.makeText(
                getContext(), "Самое маленькое время: " + minTime
                        + " у алгоритма: " + resources.getString(time[minIndex].nameId) + ", Thread: " + time[minIndex].thread,
                Toast.LENGTH_LONG
            ).show()
        }
        handler.postDelayed(runnable, 0)
    }

    /**
     * Сортировка выбранным алгоритмом
     *
     * @param sort структура для сортировки
     * @param isThread через потоки или нет
     * @param isCreateView отображать ли отсортированный список
     * @param nameId id строки с названием алгоритма
     * @receiver
     * @return время работы алгоритма в мс
     */
    fun onClickFun(sort: (array: LinkedList<Int>) -> LinkedListFragment.arrayAndTime, isThread:Boolean, isCreateView: Boolean, nameId: Int) : Long{
        var time: Long = Long.MAX_VALUE // Для времени работы
        if(isThread) {
            if(isCreateView)// если создаем отбражение, то  handler для отображения
                handler = Handler()
            val thread = Thread(Runnable {// запускам сортировку
                val mas = array.clone() as LinkedList<Int>// клонируем массив для сортировки
                val arrayAndTime: LinkedListFragment.arrayAndTime = sort(mas)// выполняем сортировку указзанной функцией
                time = arrayAndTime.time// время работы
                if(isCreateView) {// если отображаем список
                    val runnable = Runnable {
                        chengeView(
                            arrayAndTime.time,
                            resources.getString(nameId),
                            arrayAndTime.mas as LinkedList<Int>
                        )
                    }
                    handler.postDelayed(runnable, 0)
                }
            })
            thread.start()
            if(!isCreateView) {// есои не отображаем, ожидаем завершения потока для получения времени работы
                try {
                    thread.join()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            return time
        }
        else{// если не через поток
            val mas = array.clone() as LinkedList<Int>
            val arrayAndTime: LinkedListFragment.arrayAndTime = sort(mas)
            if(isCreateView) {
                chengeView(
                    arrayAndTime.time,
                    resources.getString(nameId),
                    arrayAndTime.mas as LinkedList<Int>
                )
            }
            time = arrayAndTime.time
            return time
        }
    }

    /**
     * Сортировка пузырьком
     *
     * @param array список для сортировки
     * @return класс, содержащий исходный массив и время работы
     */
    fun bubbleSort(array: LinkedList<Int>): LinkedListFragment.arrayAndTime {
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
        return LinkedListFragment.arrayAndTime(array, workTime)
    }

    /**
     * Сортировка вставками
     * *
     * @param array список для сортировки
     * @return класс, содержащий исходный массив и время работы
     */
    fun insertionSort(array: LinkedList<Int>): LinkedListFragment.arrayAndTime {
        val time = System.currentTimeMillis()
        for (i in 1 until array.size) {
            val current = array[i]
            var j = i - 1
            while (j >= 0 && current < array[j]) {
                array[j + 1] = array[j]
                j--
            }
            array[j + 1] = current
        }
        val workTime: Long = System.currentTimeMillis() - time
        return LinkedListFragment.arrayAndTime(array, workTime)
    }

    /**
     * Сортировка выбором
     *
     * @param array список для сортировки
     * @return класс, содержащий исходный массив и время работы
     */
    fun selectionSort(array: LinkedList<Int>): LinkedListFragment.arrayAndTime {
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
        return LinkedListFragment.arrayAndTime(array, workTime)
    }

    /**
     * Быстрая сортировка
     *
     * @param array список для сортировки
     * @return класс, содержащий исходный массив и время работы
     */
    fun quickSort(array: LinkedList<Int>): LinkedListFragment.arrayAndTime {
        val startIndex = 0
        val endIndex: Int = array.size - 1
        val time = System.currentTimeMillis()
        doQuickSort(array, startIndex, endIndex)
        val workTime: Long = System.currentTimeMillis() - time
        return arrayAndTime(array, workTime)
    }

    private fun doQuickSort(array: LinkedList<Int>, start: Int, end: Int) {
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
        doQuickSort(array, start, cur)
        doQuickSort(array,cur + 1, end)
    }

    /**
     * Стандартный алгоритм сортировки для структуры
     *
     * @param array список для сортировки
     * @return класс, содержащий исходный массив и время работы
     */
    fun standartSort(array: LinkedList<Int>): arrayAndTime {
        val time = System.currentTimeMillis()
        array.sort()
        val workTime: Long = System.currentTimeMillis() - time
        return arrayAndTime(array, workTime)
    }

    companion object {
        @JvmStatic
        fun newInstance(countOfElement: Int) =
            LinkedListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, countOfElement)
                }
            }
    }
}
