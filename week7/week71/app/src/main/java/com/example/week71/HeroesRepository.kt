package com.example.week71

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileWriter
import java.io.InputStream
import java.io.PrintWriter

@DelicateCoroutinesApi
class HeroesRepository(private val activity: MainActivity) {
    private lateinit var client: OkHttpClient
    private lateinit var request: Request
    private var heroesList: List<HeroData>? = mutableListOf()

    /**
     * Обновлем список героев в файле
     *
     * @param userUpdate - обновляет пользователь(true) или обновление при запуске(false)
     *
     * @return обновлен файл или нет
     */
    fun updateHeroesList(userUpdate: Boolean): Boolean {
        val heroesListFromFile = getHeroesFromFile()
        val heroesListFromApi = getHeroesFromAPI()

        val toastString: String //строка для toast
        val updated: Boolean//обновлен или нет

        //если есть список из API для сравнения
        if (!heroesListFromApi.isNullOrEmpty()) {
            if (isEqual(heroesListFromFile, heroesListFromApi)) {
                toastString = "Локальный файл не требует обновления"
                updated = false
            } else {
                writeHeroesToFile(heroesListFromApi)
                heroesList = heroesListFromApi.toMutableList()
                toastString = "Локальный файл обновлен"
                updated = true
            }

        } else {
            toastString = "Не удалось обновить: отсутствует подключение к интренету!"
            updated = false
        }

        if (userUpdate) {
            activity.lifecycleScope.launch()
            {
                Toast.makeText(activity, toastString, Toast.LENGTH_LONG).show()
            }
        }
        return updated
    }

    /**
     * Получаем список героев
     *
     * @return список героев
     */
    suspend fun getHeroesList(): List<HeroData>? {
        val file = File(PATH)
        var job: Job = Job()

        val toastString: String

        var heroesListFromFile: List<HeroData>? = listOf()
        var heroesListFromApi: List<HeroData>? = listOf()

        val jobGet = GlobalScope.launch {
            heroesListFromFile = getHeroesFromFile()
            heroesListFromApi = getHeroesFromAPI()
        }
        jobGet.start()
        jobGet.join()

        //если не пустой, то из него
        if (file.isFile && !heroesListFromFile.isNullOrEmpty()) {
            //получаем список из полученной json строки
            toastString = "Данные из файла!"
            job = GlobalScope.launch {
                heroesList = heroesListFromFile
            }
        } else if (!heroesListFromApi.isNullOrEmpty()) {// иначе пытаемся из API
            toastString = "Данные из API!"
            job = GlobalScope.launch {
                heroesList = heroesListFromApi
                writeHeroesToFile(heroesList)//обновляем
            }
        } else //инач пустой список
            toastString = "Нет подходящих данных!"

        Toast.makeText(activity, toastString, Toast.LENGTH_LONG)
            .show()
        job.start()
        job.join()
        return heroesList
    }

    /**
     * Получение списка героев из API
     *
     * @return список героев из API
     */
    private fun getHeroesFromAPI(): List<HeroData>? {
        var heroesListFromCallback: List<HeroData>? = mutableListOf()
        val connectivity: ConnectivityManager =
            activity.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        val info = connectivity.activeNetwork
        if (info != null) {
            client = OkHttpClient()
            // Объект request с url адресом
            request = Request.Builder()
                .url("$BASE_URL$HERO_STATS")
                .build()
            //делаем запрос и получаем ответ

            val response = client.newCall(request).execute()

            val moshi = Moshi.Builder().build()
            //задем ттип списка для полученных даных
            val listType =
                Types.newParameterizedType(List::class.java, HeroData::class.java)
            //задаем адаптер для списка типа heroData
            val jsonAdapter: JsonAdapter<List<HeroData>> = moshi.adapter(listType)
            heroesListFromCallback = response.body?.string()?.let {
                jsonAdapter.fromJson(it)
            }
        }
        return heroesListFromCallback
    }

    /**
     * Получение список героев из файла
     *
     * @return список героев из файла
     */
    private fun getHeroesFromFile(): List<HeroData>? {
        // файл
        val file = File(PATH)
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
            return if (inputString.length > 1) {
                //получаем список из полученной json строку
                heroesListFromFile = inputString.let { jsonAdapter.fromJson(it) }
                heroesListFromFile
            } else {
                mutableListOf()
            }
        } else {
            return mutableListOf()
        }
    }

    /**
     * Обновление файла
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

        // если данные есть, записываем в файл
        if (heroesList?.size!! > 1)
            PrintWriter(FileWriter(PATH)).use { it.write(jsonStr.toString()) }
        else // если нет, то пустая строка
            PrintWriter(FileWriter(PATH)).use { "" }
    }

    /**
     * Удаление файла
     */
    fun deleteFile() {
        val file = File(PATH)
        val toastString: String = if (file.isFile) {
            file.delete()
            "Локальный файл удален"
        } else
            "Локальный файл не найден"

        activity.lifecycleScope.launch {
            Toast.makeText(activity, toastString, Toast.LENGTH_LONG)
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
        first?.forEachIndexed { index, _ ->
            if (second?.get(index)?.equals(second[index]) == false) {
                return false
            }
        }
        return true
    }
}