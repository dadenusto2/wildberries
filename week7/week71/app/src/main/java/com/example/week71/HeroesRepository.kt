package com.example.week71

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.*


@DelicateCoroutinesApi
class HeroesRepository(val context: MainActivity) {
    private lateinit var client: OkHttpClient
    private lateinit var request: Request
    private var heroesList: List<HeroData>? = mutableListOf()

    /**
     * Обновлем список героев в Shared Preferences
     */
    fun updateHeroesList() {
        val heroesListFromFile = getHeroesFromFile()
        val heroesListFromApi = getHeroesFromAPI(true)

        if (!heroesListFromApi.isNullOrEmpty() && !heroesListFromFile.isNullOrEmpty()) {
            if (isEqual(heroesListFromFile, heroesListFromApi)) {
                context.lifecycleScope.launch() {
                    Toast.makeText(
                        context,
                        "Локальный файл не требует обновления",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                writeHeroesToFile(heroesListFromApi)
                heroesList = heroesListFromApi.toMutableList()
                context.lifecycleScope.launch() {
                    Toast.makeText(context, "Локальный файл обновлен", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    /**
     * Получаем список героев
     *
     * @return список героев
     */
    suspend fun getHeroesList(): List<HeroData>? {
        val file = File(PATH)
        val job: Job
        // получаеа героев из файла
        val heroesListFromFile = getHeroesFromFile()
        Log.d("---", heroesListFromFile?.size.toString() + " " + heroesListFromFile.isNullOrEmpty())
        //если не пустой, то из него
        if (file.isFile && !heroesListFromFile.isNullOrEmpty()) {
            Log.d("---", "Данные из файла!")
            //получаем список из полученной json строкуи
            job = GlobalScope.launch {
                heroesList = heroesListFromFile
            }
            Toast.makeText(context, "Данные из файла!", Toast.LENGTH_LONG)
                .show()
        } else {// иначе из API
            job = GlobalScope.launch {
                Log.d("---", "Данные из API!")
                heroesList = getHeroesFromAPI(false)
                writeHeroesToFile(heroesList)//обновляем
            }
            Toast.makeText(context, "Данные из API!", Toast.LENGTH_LONG)
                .show()
        }
        job.start()
        job.join()
        context.swipeRefreshLayout.isRefreshing = false
        return heroesList
    }

    /**
     * Удаляем файл
     */
    fun deleteFile() {
        val file = File(PATH)
        file.delete()
        context.lifecycleScope.launch {
            Toast.makeText(context, "Локальный файл удален", Toast.LENGTH_LONG)
                .show()
        }
    }

    /**
     * Сравнение списка ссылок
     *
     * @param first - первый список ссылок
     * @param second - первый список ссылок
     *
     * @return равны ли списки
     */
    private fun isEqual(first: List<HeroData>?, second: List<HeroData>?): Boolean {
        if (first?.size != second?.size) {
            return false
        }
        first?.forEachIndexed { index, value ->
            if (second?.get(index)?.equals(second[index]) == false) {
                return false
            }
        }
        return true
    }
    /**
     * Получаем список героев из API
     *
     * @param forUpdate - для проверки обновля списока или вывода из API
     *
     * @return список героев из API
     */
    private fun getHeroesFromAPI(forUpdate: Boolean): List<HeroData>? {
        var heroesListFromCallback: List<HeroData>? = mutableListOf()

        client = OkHttpClient()
        // Объект request с url адресом
        request = Request.Builder()
            .url("$BASE_URL$HERO_STATS")
            .build()
        //делаем запрос и получаем ответ
        //val heroesCallback = HeroesCallback(context)
        try {
            val response = client.newCall(request).execute()

            val moshi = Moshi.Builder().build()
            //задем ттип списка для полученных даных
            val listType =
                Types.newParameterizedType(List::class.java, HeroData::class.java)
            //задаем адаптер для списка типа heroData
            val jsonAdapter: JsonAdapter<List<HeroData>> = moshi.adapter(listType)
            heroesListFromCallback = response.body?.string()?.let { jsonAdapter.fromJson(it) }
        } catch (e: Exception) {
            context.lifecycleScope.launch {
                if (!forUpdate)
                    Toast.makeText(context, "Нет соединения!", android.widget.Toast.LENGTH_LONG)
                        .show()
            }
        }
        return heroesListFromCallback

    }
    /**
     * Получаем список героев из файла
     *
     * @return список героев из файла
     */
    private fun getHeroesFromFile(): List<HeroData>? {
        // файл
        val file = File(PATH)
        // проверям сущесвование пути
        if (!file.isDirectory) {
            file.parentFile?.mkdirs()
            return mutableListOf()
        }
        // если файл
        if (file.isFile) {
            client = OkHttpClient()
            val heroesListFromFile: List<HeroData>?
            val moshi = Moshi.Builder().build()
            //задем тип списка для полученных даных
            val listType =
                Types.newParameterizedType(List::class.java, HeroData::class.java)
            //задаем адаптер для списка типа heroData
            val jsonAdapter: JsonAdapter<List<HeroData>> = moshi.adapter(listType)

            val inputStream: InputStream = file.inputStream()
            // получаем строку json из файл
            val inputString = inputStream.bufferedReader().use { it.readText() }
            if (inputString.length > 1) {
                //получаем список из полученной json строку
                heroesListFromFile = inputString.let { jsonAdapter.fromJson(it) }
                return heroesListFromFile
            } else {
                return mutableListOf()
            }
        } else {
            return mutableListOf()
        }
    }

    /**
     * Обновляем файл
     *
     * @param heroesList - ланные для обновления
     */
    private fun writeHeroesToFile(heroesList: List<HeroData>?) {
        // файл
        val file = File(PATH)
        //задаем деррикторию
        file.parentFile?.mkdirs()

        val moshi = Moshi.Builder().build()

        val type = Types.newParameterizedType(List::class.java, HeroData::class.java)
        val adapterToJson = moshi.adapter<List<HeroData>>(type)
        val jsonStr = adapterToJson.toJson(heroesList)

        // если даннве есть
        if (heroesList?.size!! > 1)
            // записываем в файл
            PrintWriter(FileWriter(PATH)).use { it.write(jsonStr.toString()) }
        else// если нет, то пустая строка
            PrintWriter(FileWriter(PATH)).use { "" }
    }
}