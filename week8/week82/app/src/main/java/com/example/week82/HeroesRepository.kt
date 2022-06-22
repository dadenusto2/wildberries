package com.example.week82

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.week82.model.HeroData
import com.example.week82.model.RetrofitService
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable

/**
 * Репозиторий списка героев
 */
@DelicateCoroutinesApi
class HeroesRepository(private val context: FragmentActivity) : Serializable {
    private var heroesList: List<HeroData>? = mutableListOf()

    /**
     * Получаем список героев
     *
     * @return список героев
     */
    suspend fun getHeroesList(): List<HeroData>? {
        val job: Job
        heroesList = mutableListOf()
        var heroesFromPreferences: List<HeroData>? = listOf()
        var heroesFromAPI: List<HeroData>? = listOf()
        // получаеа героев из Shared Preferences
        val jobGet = GlobalScope.launch {
            heroesFromPreferences = getHeroesFromPref()
            heroesFromAPI = getHeroesFromAPI()
        }
        jobGet.start()
        jobGet.join()

        //если не пустой, то из него
        if (heroesFromPreferences?.isNotEmpty() == true) {
            job = GlobalScope.launch {
                heroesList = heroesFromPreferences
            }
            context.lifecycleScope.launch {
                Toast.makeText(context, "Данные из Shared Preferences!", Toast.LENGTH_LONG)
                    .show()
            }
        } else if (heroesFromAPI?.size!! > 0) { // иначе из API
            job = GlobalScope.launch {
                heroesList = getHeroesFromAPI()
            }
            context.lifecycleScope.launch {
                Toast.makeText(context, "Данные из API!", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            job = context.lifecycleScope.launch {
                Toast.makeText(context, "Нет подходящих данных!", Toast.LENGTH_LONG)
                    .show()
            }
        }
        job.start()
        job.join()
        //context.swipeRefreshLayout.isRefreshing = false
        return heroesList
    }

    /**
     * Получаем список героев из Shared Preferences
     *
     * @return список героев из Shared Preferences
     */
    fun getHeroesFromPref(): List<HeroData>? {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        //создаем запрос
        val mPrefs: SharedPreferences = context.getPreferences(MODE_PRIVATE)
        val heroFromPref: String? = mPrefs.getString(
            "HeroJson",
            null
        )
        val myType = object : TypeToken<List<HeroData>>() {}.type
        //возвращаем список героев из Shared Preferences
        return gson.fromJson(heroFromPref, myType)
    }

    /**
     * Получаем список героев из API
     *
     * @return список героев из API
     */
    @SuppressLint("CommitPrefEdits")
    fun getHeroesFromAPI(): List<HeroData>? {
        val connectivity: ConnectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        val info = connectivity.activeNetwork
        if (info != null) {
            var heroListFromApi: List<HeroData>? = mutableListOf()
            val gson = GsonBuilder()
                .setLenient()
                .create()
            //создаем запрос
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            //указываем класс для класс интерфеса для запрроса
            val service = retrofit.create(RetrofitService::class.java)
            val call: Call<List<HeroData>> = service.heroesData
            //делаем запрос
            val response = call.execute()
            writeHeroesToPref(response.body())
            heroListFromApi = response.body()
            Log.d("---", heroListFromApi?.size.toString())
            return heroListFromApi
        } else
            return mutableListOf()
    }

    /**
     * Обновлем список героев в Shared Preferences
     */
    fun updateHeroesList(userUpdate: Boolean): Boolean {
        val heroesListFromFile = getHeroesFromPref()
        val heroesListFromApi = getHeroesFromAPI()

        val toastString: String
        val updated: Boolean
        if (!heroesListFromApi.isNullOrEmpty()) {
            if (isEqual(heroesListFromFile, heroesListFromApi)) {
                toastString = "Локальный файл не требует обновления"
                updated = false
            } else {
                writeHeroesToPref(heroesListFromApi)
                heroesList = heroesListFromApi.toMutableList()
                toastString = "Локальный файл обновлен"
                updated = true
            }

        } else {
            updated = false
            toastString = "Не удалось обновить: отсутствует подключение к интренету!"
        }

        if (userUpdate) {
            context.lifecycleScope.launch()
            {
                Toast.makeText(context, toastString, Toast.LENGTH_LONG).show()
            }
        }
        return updated
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
            if (first[index].equals(second?.get(index))) {
                Log.d("---", "\n${first[index]}.\n ${second?.get(index)}")
                return false
            }
        }
        return true
    }

    /**
     * Запись данных в Shared Preferences
     *
     * @param heroesList - список для обновления
     */
    private fun writeHeroesToPref(heroesList: List<HeroData>?) {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val mPrefs: SharedPreferences = context.getPreferences(MODE_PRIVATE)

        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val json = gson.toJson(heroesList)
        // вставляем обновленный список
        prefsEditor.putString("HeroJson", json)
        prefsEditor.apply()
    }

    /**
     * Удаление Shared Preferences
     */
    fun deleteFile() {
        val mPrefs: SharedPreferences = context.getPreferences(MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        prefsEditor.clear()
        prefsEditor.apply()
        context.lifecycleScope.launch() {
            Toast.makeText(context, "Shared Preferences удален!", Toast.LENGTH_LONG)
                .show()
        }
    }
}