package com.example.additionaltask1

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

// Инструкция:
// 1) "Введите кол-во элементов" - введите кол-во элементов целым числом
// (При вводе если уже выбрана структура данных произойдет сброс и структуру надо выбрать заново)
// 2) "Выбор структуры данных":
//  a)Array
//  б)ArrayLIst
//  в)LinkedList
// 3) После выбора структуры Появятся:
//  а)"Через Thread" - выделите, если хотите делать сортировку через потоки
//  б)"Найти саммый быстрый алгоритм"(Займет некоторое время) - находит самый быстрый алгоритм
//  в)"Перемешать исходный массив" - перемешивает исходный массив
//  г)"Выберите метод сортировки" - выбор метода сортировки:
//    I)Пузырьковая сортировка
//    II)Сортировка выбором
//    III)Сортировка вставками
//    IV)Быстрая сортировка
//    V)Стандартный алгоритм сортировки для данной структуры
//  При выборе метода сортировки в правом списке появится отсортированный массив
//  д)Список слева - исходная перемешенная структура данных
//  е)Список справа - отсортированный массив с указание времени работы в мс и названием алгоритма сортировки

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // список структур данных
        val spinner: Spinner = findViewById(R.id.spn_data)
        val data = arrayOf(// массив строк структур
            "Выберите структуру данных",
            resources.getString(R.string.array_name),
            resources.getString(R.string.arraylist_name),
            resources.getString(R.string.linkedlist_name)
        )
        spinner.adapter =// задаем адаптер
            ArrayAdapter(this, R.layout.spinner_item, R.id.tv_spinner, data)

        val countString = findViewById<EditText>(R.id.et_countOfElem)
        countString.addTextChangedListener(// если текст изменен и выбрана структура сбрасывается структура
            afterTextChanged = {
                if(spinner.selectedItemId != 0.toLong())
                    spinner.setSelection(0)
            }
        )
        // обработка выбора списка
        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    // Получаем выбранный объект
                    when (parent.getItemAtPosition(position) as String) {
                        data[0] -> {// если выбран первый элемент, то убираем фрагмент
                            hideKeyboard()
                            val fm: FragmentManager = supportFragmentManager

                            val fragment = fm.findFragmentById(R.id.fragmentContainer)

                            if (fragment != null)
                                fm.beginTransaction().remove(fragment).commit()
                        }
                        data[1] -> {// сортировка array
                            //hideKeyboard()
                            arraySort { ArrayFragment() }
                        }
                        data[2] -> {// сортировка arrayList
                            hideKeyboard()
                            arraySort { ArrayListFragment() }
                        }
                        data[3] ->{// сортировка LinkedList
                            hideKeyboard()
                            arraySort { LinkedListFragment() }
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        spinner.onItemSelectedListener = itemSelectedListener
    }

    /**
     * Запуск фрагмета
     *
     * @param f фрагмент
     * @receiver
     */
     fun arraySort(f: () -> Fragment){

         val fm: FragmentManager = supportFragmentManager

         val countString = findViewById<EditText>(R.id.et_countOfElem).text.toString()

         var fragment = fm.findFragmentById(R.id.fragmentContainer)

         if (countString != "") {
             if(fragment != null)
                fm.beginTransaction().remove(fragment).commit()
             val countOfELem = Integer.parseInt(countString)// получаем кол-во элементов

             val bundle = Bundle()
             bundle.putInt("param1", countOfELem)

             fragment = f()// берем фрагмент из параметров

             fragment.arguments = bundle//создаем выбранный фрагмент
             fm.beginTransaction()
                 .add(R.id.fragmentContainer, fragment)
                 .commit()
         }
         else// если пустое кол-во элементов , то задаем ошибку
             findViewById<EditText>(R.id.et_countOfElem).setError("Введите кол-во элементов")
     }
    fun hideKeyboard() {
        //Находим View с фокусом, так мы сможем получить правильный window token
        //Если такого View нет, то создадим одно, это для получения window token из него
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = findViewById<EditText>(R.id.et_countOfElem)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}